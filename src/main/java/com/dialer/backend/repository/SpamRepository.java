package com.dialer.backend.repository;

import com.dialer.backend.model.SpamEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA automatically generates all SQL queries.
 * No implementation needed — Spring handles it all.
 */
@Repository
public interface SpamRepository extends JpaRepository<SpamEntry, Long> {

    // SELECT * FROM spam_entries WHERE phone_number = ?
    Optional<SpamEntry> findByPhoneNumber(String phoneNumber);
}
