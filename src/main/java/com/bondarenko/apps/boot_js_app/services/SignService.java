package com.bondarenko.apps.boot_js_app.services;

import com.bondarenko.apps.boot_js_app.domain.entities.Author;
import com.bondarenko.apps.boot_js_app.repositories.AuthorRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@PropertySource("classpath:application.properties")
public class SignService implements ISignService {
    private final AuthorRepository authorRepository;
    @Value("${security.local-parameter}")
    private String localParameter;

    @Override
    public boolean register(String login, String password, String name, String email) {
        String hashedPassword = BCrypt.hashpw(password+localParameter, BCrypt.gensalt());
        if (authorRepository.existsById(login) || authorRepository.existsAuthorByEmail(email)) {
            return false;
        } else {
            authorRepository.save(new Author(login, hashedPassword, name, email, Author.READER));
            return true;
        }
    }

    @Override
    public Author authorize(String login) {
        Optional<Author> optionalAuthor = authorRepository.findById(login);
        return optionalAuthor.orElse(null);
    }

    @Override
    public Author authenticate(String login, String password) {
        Optional<Author> optionalAuthor = authorRepository.findById(login);
        if (optionalAuthor.isPresent()) {
            if (BCrypt.checkpw(password+localParameter, optionalAuthor.get().getPassword())) {
                return optionalAuthor.get();
            }
        }
        return null;
    }

    @Override
    public boolean checkLogin(String login) {
        return authorRepository.existsById(login);
    }

    @Override
    public boolean checkEmail(String email) {
        return authorRepository.existsAuthorByEmail(email);
    }
}
