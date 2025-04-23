package com.bumsoap.notes.service.impl;

import com.bumsoap.notes.models.Note;
import com.bumsoap.notes.repo.NoteRepo;
import com.bumsoap.notes.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    private NoteRepo noteRepo;

    @Override
    public Note createFor(String username, String content) {
        Note note = new Note();
        note.setContent(content);
        note.setOwnerUsername(username);
        return noteRepo.save(note);
    }

    @Override
    public Note updateFor(Long noteId, String content, String username) {
        Note note = noteRepo.findById(noteId).orElseThrow(
                () -> new RuntimeException("노트 찾기 실패"));
        note.setContent(content);
        return noteRepo.save(note);
    }

    @Override
    public void deleteFor(Long noteId) {
        noteRepo.deleteById(noteId);
    }

    @Override
    public List<Note> getNotesFor(String username) {
        return noteRepo.findByOwnerUsername(username);
    }

}
