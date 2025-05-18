package com.bumsoap.notes.service.impl;

import com.bumsoap.notes.models.Note;
import com.bumsoap.notes.service.NoteAudit;
import org.springframework.stereotype.Service;

@Service
public class NoteAuditImpl implements NoteAudit {
  @Override
  public void logCreate(String username, Note note) {
  }

  @Override
  public void logUpdate(String username, Note note) {
  }

  @Override
  public void logDelete(String username, Note note) {
  }
}
