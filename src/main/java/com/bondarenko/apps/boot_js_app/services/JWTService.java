package com.bondarenko.apps.boot_js_app.services;

import com.bondarenko.apps.boot_js_app.entities.Author;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService implements IJWTService {
    private ISignService service;
    private String key = "GYu7uiUYT87ghUG6gG7IK65o0YT7Jh98H7y7H8Mj8h8H65UC32DD1gfk4d678h8JDkl5fy89FHsfH89fs0if8T6d80sif098SDHffDFfGV786gUH9yN87G7tJP0987HNu7tTHn88jklFP7gh5dfHUh797";

    public JWTService(ISignService service) {
        this.service = service;
    }

    @Override
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
    public Author getAuthorFromToken(String token) throws Exception{
        try {
            return (Author) Jwts.parser().setSigningKey(key).parse(token).getBody();
        } catch (Exception ex) {
            throw new Exception("Token corrupted!");
        }
    }
}
