package com.bumsoap.notes.repo;

import com.bumsoap.notes.models.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteAuditRepo
    extends JpaRepository<AuditLog, Long> {
}
