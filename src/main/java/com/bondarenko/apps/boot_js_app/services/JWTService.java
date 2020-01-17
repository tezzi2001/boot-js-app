package com.bondarenko.apps.boot_js_app.services;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bondarenko.apps.boot_js_app.entities.Author;
import com.bondarenko.apps.boot_js_app.entities.JWT;
import com.bondarenko.apps.boot_js_app.repositories.JWTRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;

@Service
public class JWTService implements IJWTService {
    private ISignService service;
    private JWTRepository jwtRepository;
    private Key key = MacProvider.generateKey(SignatureAlgorithm.HS256);

    public JWTService(ISignService service, JWTRepository jwtRepository) {
        this.service = service;
        this.jwtRepository = jwtRepository;
    }

    @Override
    public Map<String, String> refreshTokens(String oldRefreshToken, String fingerprint) {
        Map<String, String> tokens = new HashMap<>();

        if (oldRefreshToken == null || fingerprint == null) {
            tokens.put("status", "NULL_FIELD");
            return tokens;
        }

        Optional<JWT> optSession = jwtRepository.findJWTByRefreshToken(oldRefreshToken);
        JWT session;
        if (optSession.isPresent()) {
            session = optSession.get();
            jwtRepository.deleteJWTByRefreshToken(oldRefreshToken);
        } else {
            tokens.put("status", "INVALID_TOKEN");
            return tokens;
        }

        if (session.getExpiresAt().before(new Date())) {
            tokens.put("status", "TOKEN_EXPIRED");
            return tokens;
        }

        if (!fingerprint.equals(session.getFingerprint())) {
            tokens.put("status", "INVALID_SESSION");
            return tokens;
        }

        Author author = service.authorize(session.getLogin());
        if (author == null) {
            tokens.put("status", "INVALID_USER");
            return tokens;
        }

        String refreshToken = generateRefreshToken();
        session.setUpdatedAt(new Date());
        session.setExpiresAt(new Date(System.currentTimeMillis()+7*60*60*1000));
        session.setRefreshToken(refreshToken);
        jwtRepository.save(session);
        tokens.put("status", "OK");
        tokens.put("accessToken", generateAccessToken(author));
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    @Override
    public Map<String, String> getTokens(String login, String password, String fingerprint) {
        Map<String, String> tokens = new HashMap<>();

        if (jwtRepository.existsJWTByFingerprint(fingerprint)) jwtRepository.deleteJWTByFingerprint(fingerprint);

        if (login == null || password == null || fingerprint == null) {
            tokens.put("status", "NULL_FIELD");
            return tokens;
        }
        Author author = service.authorize(login, password);
        if (author == null) {
            tokens.put("status", "INVALID_USER");
            return tokens;
        }
        String refreshToken = generateRefreshToken();
        jwtRepository.save(new JWT(login, refreshToken, fingerprint, new Date(System.currentTimeMillis()+7*60*60*1000), new Date(), new Date()));
        tokens.put("status", "OK");
        tokens.put("accessToken", generateAccessToken(author));
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    @Override
    @Deprecated
    public String getToken(String login, String password) {
        if (login == null || password == null) return null;
        Author author = service.authorize(login, password);
        if (author == null) return null;
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("login", author.getLogin());
        tokenData.put("name", author.getName());
        tokenData.put("email", author.getEmail());
        tokenData.put("role", author.getRole());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 100);
        tokenData.put("token_expiration_date", calendar.getTime());
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setExpiration(calendar.getTime());
        jwtBuilder.setClaims(tokenData);
        return jwtBuilder.signWith(SignatureAlgorithm.HS512, key).compact();
    }

    @Override
    @Deprecated
    public Author getAuthorFromToken(String token) throws Exception{
//        try {
//            return (Author) Jwts.parser().setSigningKey(key).parse(token).getBody();
//        } catch (Exception ex) {
//            throw new Exception("Token corrupted!");
//        }
        Algorithm algorithm = Algorithm.HMAC256("secret");
        try {
            JWTVerifier verifier = com.auth0.jwt.JWT.require(algorithm)
//                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Base64.getDecoder().decode(jwt.getPayload()), Author.class);
        } catch (JWTVerificationException exception){
            System.out.println("Invalid signature");
            return null;
        }
    }

    private String generateRefreshToken() {
        Date expDate = new Date(System.currentTimeMillis()+7*60*60*1000);
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("token_expiration_date", expDate);
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setExpiration(expDate);
        jwtBuilder.setClaims(tokenData);
        return jwtBuilder.signWith(SignatureAlgorithm.HS256, key).compact();
    }

    private String generateAccessToken(Author author) {
//        Map<String, Object> tokenData = new HashMap<>();
//        tokenData.put("login", author.getLogin());
//        tokenData.put("name", author.getName());
//        tokenData.put("email", author.getEmail());
//        tokenData.put("role", author.getRole());
//        Date date = new Date(System.currentTimeMillis()+20*60*1000);
//        JwtBuilder jwtBuilder = Jwts.builder();
//        jwtBuilder.setExpiration(date);
//        jwtBuilder.setClaims(tokenData);
//        return jwtBuilder.signWith(SignatureAlgorithm.HS256, key).compact();
        Algorithm algorithm = Algorithm.HMAC256("secret");
        String token = com.auth0.jwt.JWT
                .create()
                .withClaim("login", author.getLogin())
                .withClaim("name", author.getName())
                .withClaim("email", author.getEmail())
                .withClaim("role", author.getRole())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis()+20*60*1000))
                .sign(algorithm);
        try {
            JWTVerifier verifier = com.auth0.jwt.JWT.require(algorithm)
//                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
        } catch (JWTVerificationException exception){
            System.out.println("Invalid signature");
        }
        return token;
    }
}
