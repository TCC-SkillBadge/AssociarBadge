package com.example.badgeassignment.repository;

import com.example.badgeassignment.model.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    Optional<Badge> findByConfirmationToken(String confirmationToken);
}
