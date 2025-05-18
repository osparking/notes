package com.bumsoap.notes.service;

import com.bumsoap.notes.models.Note;

public interface NoteAudit {
  void logCreate(String username, Note note);

  void logUpdate(String username, Note note);

  void logDelete(String username, Note note);
}
