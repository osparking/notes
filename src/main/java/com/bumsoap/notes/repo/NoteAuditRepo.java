package com.bumsoap.notes.repo;

import com.bumsoap.notes.models.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteAuditRepo
    extends JpaRepository<AuditLog, Long> {
  List<AuditLog> findByNoteId(Long noteId);
}
