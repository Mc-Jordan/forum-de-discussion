package com.mc_jordan.forum_de_discussion.repositories;

import com.mc_jordan.forum_de_discussion.entites.Validation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ValidationRepository extends JpaRepository<Validation, Integer> {
    Optional<Validation> findByCode(String code);
}
