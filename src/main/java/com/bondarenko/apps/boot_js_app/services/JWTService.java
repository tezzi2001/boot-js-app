package com.bondarenko.apps.boot_js_app.services;

import com.bondarenko.apps.boot_js_app.entities.Author;
import com.bondarenko.apps.boot_js_app.entities.JWT;
import com.bondarenko.apps.boot_js_app.repositories.JWTRepository;
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
    private Key key = MacProvider.generateKey();

    public JWTService(ISignService service, JWTRepository jwtRepository) {
        this.service = service;
        this.jwtRepository = jwtRepository;
    }

    @Override
    public Map<String, String> refreshTokens(String oldRefreshToken, String fingerprint) {
        Map<String, String> tokens = new HashMap<>();
        JWT session = jwtRepository.deleteJWTByRefreshToken(oldRefreshToken);

        if (session == null) {
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

        tokens.put("status", "OK");
        tokens.put("accessToken", generateAccessToken(author));
        tokens.put("refreshToken", generateRefreshToken());

        return tokens;
    }


    @Override
    public Map<String, String> getTokens(String login, String password, String fingerprint) {
        Map<String, String> tokens = new HashMap<>();
        if (login == null || password == null || fingerprint == null) return null;
        Author author = service.authorize(login, password);
        if (author == null) return null;
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
        try {
            return (Author) Jwts.parser().setSigningKey(key).parse(token).getBody();
        } catch (Exception ex) {
            throw new Exception("Token corrupted!");
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
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("login", author.getLogin());
        tokenData.put("name", author.getName());
        tokenData.put("email", author.getEmail());
        tokenData.put("role", author.getRole());
        Date date = new Date(System.currentTimeMillis()+20*60*1000);
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setExpiration(date);
        jwtBuilder.setClaims(tokenData);
        return jwtBuilder.signWith(SignatureAlgorithm.HS256, key).compact();
    }
}
