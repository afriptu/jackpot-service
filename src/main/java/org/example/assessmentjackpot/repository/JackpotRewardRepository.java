package org.example.assessmentjackpot.repository;

import org.example.assessmentjackpot.model.JackpotReward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JackpotRewardRepository extends JpaRepository<JackpotReward, Long> {
    boolean existsByBetId(Long betId);
}