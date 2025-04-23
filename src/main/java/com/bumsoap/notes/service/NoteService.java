package com.bumsoap.notes.service;

import com.bumsoap.notes.models.Note;

import java.util.List;

public interface NoteService {
    Note createFor(String username, String content);

    Note updateFor(Long noteId, String content, String username);

    void deleteFor(Long noteId);

    List<Note> getNotesFor(String username);
}
