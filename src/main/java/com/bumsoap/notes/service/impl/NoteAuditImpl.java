package com.bumsoap.notes.service.impl;

import com.bumsoap.notes.models.AuditLog;
import com.bumsoap.notes.models.Note;
import com.bumsoap.notes.service.NoteAudit;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NoteAuditImpl implements NoteAudit {
  @Override
  public void logCreate(String username, Note note) {
    var noteLog = new AuditLog();
    noteLog.setNoteId(note.getId());
    noteLog.setNoteContent(note.getContent());
    noteLog.setUsername(username);
    noteLog.setAction("create");
    noteLog.setTimestamp(LocalDateTime.now());

  }

  @Override
  public void logUpdate(String username, Note note) {
  }

  @Override
  public void logDelete(String username, Note note) {
  }
}
