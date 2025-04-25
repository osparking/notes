package com.bumsoap.notes.repo;

import com.bumsoap.notes.models.AppRole;
import com.bumsoap.notes.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
