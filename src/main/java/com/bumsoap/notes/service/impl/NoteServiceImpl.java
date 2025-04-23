package com.bumsoap.notes.service.impl;

import com.bumsoap.notes.models.Note;
import com.bumsoap.notes.repo.NoteRepo;
import com.bumsoap.notes.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    private NoteRepo noteRepo;

    @Override
    public Note createFor(String username, String content) {
        Note note = new Note();
        note.setContent(content);
        note.setOwnerUsername(username);
        Note saved = noteRepo.save(note);
        return saved;
    }

}
