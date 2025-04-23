package com.bumsoap.notes.service;

import com.bumsoap.notes.models.Note;

public interface NoteService {
    Note createFor(String username, String content);

    Note updateFor(Long noteId, String content, String username);
}
