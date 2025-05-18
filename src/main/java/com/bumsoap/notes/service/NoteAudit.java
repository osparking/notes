package com.bumsoap.notes.service;

import com.bumsoap.notes.models.AuditLog;
import com.bumsoap.notes.models.Note;

import java.util.List;

public interface NoteAudit {
  void logCreate(String username, Note note);

  void logUpdate(String username, Note note);

  void logDelete(String username, Long noteId);

  List<AuditLog> getAllAuditLogs();

  List<AuditLog> getAuditLogsFor(Long noteId);
}
