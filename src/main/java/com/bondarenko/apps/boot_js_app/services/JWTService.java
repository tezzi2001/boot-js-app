package com.bondarenko.apps.boot_js_app.services;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bondarenko.apps.boot_js_app.domain.entities.Author;
import com.bondarenko.apps.boot_js_app.domain.entities.JWT;
import com.bondarenko.apps.boot_js_app.repositories.JWTRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class JWTService implements IJWTService {
    private ISignService service;
    private JWTRepository jwtRepository;
    private Algorithm algorithm;

    public JWTService(ISignService service, JWTRepository jwtRepository) {
        this.service = service;
        this.jwtRepository = jwtRepository;
        algorithm = Algorithm.HMAC256(KeyGenerators.secureRandom(50).generateKey());
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
    public Author getAuthorFromToken(String token) {
        try {
            JWTVerifier verifier = com.auth0.jwt.JWT.require(algorithm)
                    .withIssuer("heroku:spring-boot-rest-api-app")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Base64.getDecoder().decode(jwt.getPayload()), Author.class);
        } catch (JWTVerificationException e){
            System.out.println("Invalid signature");
            return null;
        } catch (IOException e) {
            System.out.println("Can not parse JSON");
            return null;
        }
    }

    private String generateRefreshToken() {
        return com.auth0.jwt.JWT
                .create()
                .withIssuer("heroku:spring-boot-rest-api-app")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis()+7*60*60*1000)) // Token expires in 7 days
                .sign(algorithm);
    }

    private String generateAccessToken(Author author) {
        return com.auth0.jwt.JWT
                .create()
                .withClaim("login", author.getLogin())
                .withClaim("name", author.getName())
                .withClaim("email", author.getEmail())
                .withClaim("role", author.getRole())
                .withIssuer("heroku:spring-boot-rest-api-app")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis()+20*60*1000)) // Token expires in 20 minutes
                .sign(algorithm);
    }
}
