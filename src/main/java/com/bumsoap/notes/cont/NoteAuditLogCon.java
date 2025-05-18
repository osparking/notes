package com.bumsoap.notes.cont;

import com.bumsoap.notes.models.AuditLog;
import com.bumsoap.notes.service.NoteAudit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class NoteAuditLogCon {
  @Autowired
  private NoteAudit noteAudit;

  @GetMapping
  public List<AuditLog> getAllAuditLogs() {
    return noteAudit.getAllAuditLogs();
  }

  @GetMapping("/note/{noteId}")
  public List<AuditLog> getAuditLogsFor(@PathVariable Long noteId) {
    return noteAudit.getAuditLogsFor(noteId);
  }
}
