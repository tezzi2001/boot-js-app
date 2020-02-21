package com.bondarenko.apps.boot_js_app.controllers;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.bondarenko.apps.boot_js_app.domain.entities.Author;
import com.bondarenko.apps.boot_js_app.domain.entities.Note;
import com.bondarenko.apps.boot_js_app.services.IJWTService;
import com.bondarenko.apps.boot_js_app.services.INoteService;
import com.bondarenko.apps.boot_js_app.services.ISignService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a rest controller class
 * This class delegates {@link INoteService}
 * @author Bohdan Bondarenko
 * @version 1.0.1
 */
@AllArgsConstructor
@RestController
public class NoteController {
    private INoteService noteService;
    private IJWTService jwtService;
    private ISignService signService;

    /**
     * Returns all notes from DB
     * @see INoteService#getNotes()
     * @return array of JSON objects with fields "id", "briefDescription", "fullDescription", "date" and "title"
     */
    @GetMapping("/getAll")
    public List<Note> getNotes() {
        return noteService.getNotes();
    }

    /**
     * Returns a note from DB specified by its id
     * @see INoteService#getNoteById(int)
     * @param id An id that specifies the note
     * @return JSON object with fields "id", "briefDescription", "fullDescription", "date" and "title"
     */
    @GetMapping("/get{id}")
    public Note getNote(@PathVariable int id) {
        return noteService.getNoteById(id);
    }

    /**
     * Creates new record in DB
     * @see INoteService#addNote(Note, String)
     * @return JSON object with fields "id", "isAdded", "brief_description", "full_description", "title", and "date" or JSON object with field "isAdded" or HTTP response with empty body and status 400
     */
    @PostMapping("/add")
    public Map addNote(String token, String briefDescription, String fullDescription, String title) {
        try {
            Map<String, String> responseBody = new HashMap<>();
            Author author = jwtService.getAuthorFromToken(token);
            Note note = noteService.addNote(new Note(briefDescription, fullDescription, new Date(), title, 0), author.getLogin());

            if (note == null) {
                responseBody.put("isAdded", "false");
            } else {
                responseBody.put("isAdded", "true");
                responseBody.put("brief_description", note.getBriefDescription());
                responseBody.put("full_description", note.getFullDescription());
                responseBody.put("title", note.getTitle());
                responseBody.put("date", note.getDate());
                responseBody.put("id", note.getId().toString());
            }
            return responseBody;
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Received JSON has null parameter(s)", e);
        } catch (JWTVerificationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid signature", e);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Can not parse JSON", e);
        }
    }

    /**
     * Deletes the record in DB specified by id
     * @see INoteService#deleteNote(int)
     * @param id An id that specifies the note
     * @return JSON object with field "isDeleted" or HTTP response with empty body and status 400
     */
    @PostMapping("/delete{id}")
    public Map deleteNote(String token, @PathVariable Integer id) {
        try {
            Author author = jwtService.getAuthorFromToken(token);
            boolean isAdmin = author.getRole().equals(Author.ADMINISTRATOR);
            boolean isLoginExists = signService.checkLogin(author.getLogin());
            boolean isIdExists = noteService.existsById(id);

            if (!(isAdmin && isLoginExists && isIdExists))  throw new UnsupportedOperationException("Admin = " + isAdmin + " login = " + isLoginExists + "ID = " + isIdExists);

            return new HashMap<String, Boolean>() {{
                put("isDeleted", noteService.deleteNote(id));
            }};
        } catch (JWTVerificationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid signature", e);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Can not parse JSON", e);
        } catch (UnsupportedOperationException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not enough permissions to perform action", e);
        }
    }

    @PostMapping("/inc")
    public Map incLikes(Integer id, String token) {
        try {
            Author author = jwtService.getAuthorFromToken(token);
            return noteService.incLikes(id, author, token);
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Received JSON has null parameter(s)", e);
        } catch (JWTVerificationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid signature", e);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Can not parse JSON", e);
        }
    }

    @PostMapping("/dec")
    public Map decLikes(Integer id, String token) {
        try {
            Author author = jwtService.getAuthorFromToken(token);
            return noteService.decLikes(id, author, token);
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Received JSON has null parameter(s)", e);
        } catch (JWTVerificationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid signature", e);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Can not parse JSON", e);
        }
    }
}
