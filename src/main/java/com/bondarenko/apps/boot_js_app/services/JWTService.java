package com.bondarenko.apps.boot_js_app.services;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bondarenko.apps.boot_js_app.domain.entities.Author;
import com.bondarenko.apps.boot_js_app.domain.entities.Session;
import com.bondarenko.apps.boot_js_app.domain.json.JWT;
import com.bondarenko.apps.boot_js_app.repositories.JWTRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class JWTService implements IJWTService {
    private ISignService service;
    private JWTRepository jwtRepository;
    private Algorithm algorithm;
    private String issuer;

    private final int ACCESS_TOKEN_DURATION = 1*60*1000; // 1 minutes
    private final int REFRESH_TOKEN_DURATION = 90*1000; // Token expires in 1.5 minutes

    public JWTService(ISignService service, JWTRepository jwtRepository) {
        this.service = service;
        this.jwtRepository = jwtRepository;
        algorithm = Algorithm.HMAC256(KeyGenerators.secureRandom(50).generateKey());
        issuer = "heroku:spring-boot-rest-api-app";
    }

    @Override
    public Map<String, String> refreshTokens(String oldRefreshToken, String fingerprint) {
        Map<String, String> tokens = new HashMap<>();

        Optional<Session> optSession = jwtRepository.findJWTByRefreshToken(oldRefreshToken);
        Session session;
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
        session.setExpiresAt(new Date(System.currentTimeMillis()+REFRESH_TOKEN_DURATION));
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

        Author author = service.authenticate(login, password);
        if (author == null) {
            tokens.put("status", "INVALID_USER");
            return tokens;
        }
        String refreshToken = generateRefreshToken();
        jwtRepository.save(new Session(login, refreshToken, fingerprint, new Date(System.currentTimeMillis()+REFRESH_TOKEN_DURATION), new Date(), new Date()));
        tokens.put("status", "OK");
        tokens.put("accessToken", generateAccessToken(author));
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    @Override
    public Author getAuthorFromToken(String token) throws IOException {
        return getJWTFromToken(token).toAuthor();
    }

    @Override
    @SneakyThrows
    public String getAccessTokenWithNewLikedNotesId(String token, Author author) {
        JWT jwt = getJWTFromToken(token);
        return com.auth0.jwt.JWT
                .create()
                .withClaim("login", author.getLogin())
                .withClaim("name", author.getName())
                .withClaim("email", author.getEmail())
                .withClaim("role", author.getRole())
                .withClaim("likedNotesId", author.getNotesId().toString())
                .withIssuer(issuer)
                .withIssuedAt(new Date(jwt.getIat()))
                .withExpiresAt(new Date(jwt.getExp()))
                .sign(algorithm);
    }

    private String generateRefreshToken() {
        return com.auth0.jwt.JWT
                .create()
                .withIssuer(issuer)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis()+REFRESH_TOKEN_DURATION))
                .sign(algorithm);
    }

    private String generateAccessToken(Author author) {
        return com.auth0.jwt.JWT
                .create()
                .withClaim("login", author.getLogin())
                .withClaim("name", author.getName())
                .withClaim("email", author.getEmail())
                .withClaim("role", author.getRole())
                .withClaim("likedNotesId", author.getNotesId().toString())
                .withIssuer(issuer)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis()+ACCESS_TOKEN_DURATION))
                .sign(algorithm);
    }

    private JWT getJWTFromToken(String token) throws IOException {
        JWTVerifier verifier = com.auth0.jwt.JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(Base64.getDecoder().decode(jwt.getPayload()), JWT.class);
    }
}
