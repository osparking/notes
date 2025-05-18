package com.bumsoap.notes.service.impl;

import com.bumsoap.notes.models.AuditLog;
import com.bumsoap.notes.models.Note;
import com.bumsoap.notes.repo.NoteAuditRepo;
import com.bumsoap.notes.service.NoteAudit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NoteAuditImpl implements NoteAudit {
  @Autowired
  private NoteAuditRepo noteAuditRepo;

  @Override
  public void logCreate(String username, Note note) {
    var noteLog = new AuditLog();
    noteLog.setNoteId(note.getId());
    noteLog.setNoteContent(note.getContent());
    noteLog.setUsername(username);
    noteLog.setAction("create");
    noteLog.setTimestamp(LocalDateTime.now());
    noteAuditRepo.save(noteLog);
  }

  @Override
  public void logUpdate(String username, Note note) {
    var noteLog = new AuditLog();
    noteLog.setNoteId(note.getId());
    noteLog.setNoteContent(note.getContent());
    noteLog.setUsername(username);
    noteLog.setAction("갱신");
    noteLog.setTimestamp(LocalDateTime.now());
    noteAuditRepo.save(noteLog);
  }

  @Override
  public void logDelete(String username, Long noteId) {
    var noteLog = new AuditLog();
    noteLog.setNoteId(noteId);
    noteLog.setUsername(username);
    noteLog.setAction("삭제");
    noteLog.setTimestamp(LocalDateTime.now());
    noteAuditRepo.save(noteLog);
  }
}
