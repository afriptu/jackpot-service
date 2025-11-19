package org.example.assessmentjackpot.repository;

import org.example.assessmentjackpot.model.JackpotContribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JackpotContributionRepository extends JpaRepository<JackpotContribution, Long> {
    boolean existsByBetId(Long betId);
}