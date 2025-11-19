package org.example.assessmentjackpot.repository;

import org.example.assessmentjackpot.model.Jackpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JackpotRepository extends JpaRepository<Jackpot, Long> {
}