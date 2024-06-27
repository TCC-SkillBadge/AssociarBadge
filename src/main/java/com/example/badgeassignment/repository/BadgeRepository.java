package com.example.badgeassignment.repository;

import com.example.badgeassignment.model.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
}
