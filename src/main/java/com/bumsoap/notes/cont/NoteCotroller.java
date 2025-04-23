package com.bumsoap.notes.cont;

import com.bumsoap.notes.models.Note;
import com.bumsoap.notes.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteCotroller {
    @Autowired
    private NoteService noteService;

    @PostMapping
    public Note createNote(@RequestBody String content,
                           @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        System.out.println("유저 상세:" + username);
        return noteService.createFor(username, content);
    }

    @GetMapping
    public List<Note> getNotesOf(@AuthenticationPrincipal UserDetails details) {
        String username = details.getUsername();
        System.out.println("유저 상세:" + username);
        return noteService.getNotesFor(username);
    }

    @PutMapping("/{noteId}")
    public Note updateNote(@PathVariable Long noteId,
                           @RequestBody String newNote,
                           @AuthenticationPrincipal UserDetails details) {
        String username = details.getUsername();
        return noteService.updateFor(noteId, newNote, username);
    }

    @DeleteMapping("/{noteId}")
    public void deleteNote(@PathVariable Long noteId,
                           @AuthenticationPrincipal UserDetails details) {
        String username = details.getUsername();
        noteService.deleteFor(noteId);
    }

}
