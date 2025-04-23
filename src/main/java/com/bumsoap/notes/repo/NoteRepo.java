package com.bumsoap.notes.repo;

import com.bumsoap.notes.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepo extends JpaRepository<Note, Long> {
    List<Note> findByOwnerUsername(String ownerUsername);
}
