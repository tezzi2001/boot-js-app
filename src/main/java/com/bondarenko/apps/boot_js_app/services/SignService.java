package com.bondarenko.apps.boot_js_app.services;

import com.bondarenko.apps.boot_js_app.entities.Author;
import com.bondarenko.apps.boot_js_app.repositories.AuthorRepository;
import com.bondarenko.apps.boot_js_app.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@PropertySource("classpath:application.properties")
public class SignService implements ISignService {
    private AuthorRepository authorRepository;
    @Value("${security.local-parameter}")
    private String localParameter;

    @Autowired
    public void setAuthorRepository(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public boolean register(String login, String password, String name, String email) {
        String hashedPassword = BCrypt.hashpw(password+localParameter, BCrypt.gensalt());
        if (authorRepository.existsById(login) || authorRepository.existsAuthorByEmail(email)) {
            return false;
        } else {
            Author author = new Author(login, hashedPassword, name, email);
            authorRepository.save(author);
            return true;
        }
    }

    @Override
    public Author authorize(String login, String password) {
        Optional<Author> optionalAuthor = authorRepository.findById(login);
        if (optionalAuthor.isPresent()) {
            if (BCrypt.checkpw(password+localParameter, optionalAuthor.get().getPassword())) {
                return optionalAuthor.get();
            } else {
                return null;
            }
        } else {
            return null;
        }
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
