package org.example.assessmentjackpot.service;

import org.example.assessmentjackpot.dto.BetDto;
import org.example.assessmentjackpot.dto.JackpotEvaluationRequest;
import org.example.assessmentjackpot.model.*;
import org.example.assessmentjackpot.repository.JackpotContributionRepository;
import org.example.assessmentjackpot.repository.JackpotRepository;
import org.example.assessmentjackpot.repository.JackpotRewardRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class JackpotServiceIntegrationTest {

    @Autowired
    private JackpotService jackpotService;

    @Autowired
    private JackpotRepository jackpotRepository;

    @Autowired
    private JackpotContributionRepository contributionRepository;

    @Autowired
    private JackpotRewardRepository rewardRepository;

    @AfterEach
    void cleanup() {
        rewardRepository.deleteAll();
        contributionRepository.deleteAll();
        jackpotRepository.deleteAll();
    }

    @Test
    void processContribution_savesContribution_and_updatesPool_forFixedContribution() {
        // given
        Jackpot jackpot = new Jackpot(100L, "FixedJ", new BigDecimal("10000.00"),
                ContributionType.FIXED, RewardType.FIXED);
        jackpotRepository.save(jackpot);

        BetDto bet = new BetDto();
        bet.setBetId(1001L);
        bet.setUserId(42L);
        bet.setJackpotId(jackpot.getId());
        bet.setBetAmount(new BigDecimal("100.00"));

        // when
        jackpotService.processContribution(bet);

        // then: contribution saved
        List<JackpotContribution> contributions = contributionRepository.findAll();
        assertThat(contributions).hasSize(1);
        JackpotContribution saved = contributions.get(0);
        assertThat(saved.getBetId()).isEqualTo(1001L);
        assertThat(saved.getUserId()).isEqualTo(42L);
        assertThat(saved.getStakeAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
        // fixed 10% -> 10.00
        assertThat(saved.getContributionAmount()).isEqualByComparingTo(new BigDecimal("10.00"));

        // jackpot pool increased by contribution
        Optional<Jackpot> opt = jackpotRepository.findById(jackpot.getId());
        assertThat(opt).isPresent();
        Jackpot updated = opt.get();
        assertThat(updated.getCurrentPool()).isEqualByComparingTo(new BigDecimal("10010.00"));
    }

    @Test
    void evaluateWin_resetsPool_and_persistsReward_whenVariableReward_hitsLimit() {
        // given: initial pool small, but current pool set high to hit deterministic 100% win
        Jackpot jackpot = new Jackpot(200L, "BigWinJ", new BigDecimal("5000.00"),
                ContributionType.FIXED, RewardType.VARIABLE);
        jackpot.setCurrentPool(new BigDecimal("1000000")); // force deterministic 100% win
        jackpotRepository.save(jackpot);

        JackpotEvaluationRequest req = new JackpotEvaluationRequest();
        req.setBetId(2001L);
        req.setUserId(77L);
        req.setJackpotId(jackpot.getId());

        // when
        BigDecimal rewardAmount = jackpotService.evaluateWin(req);

        // then: reward amount equals previous currentPool
        assertThat(rewardAmount).isEqualByComparingTo(new BigDecimal("1000000"));

        // jackpot reset to initial pool
        Optional<Jackpot> opt = jackpotRepository.findById(jackpot.getId());
        assertThat(opt).isPresent();
        Jackpot updated = opt.get();
        assertThat(updated.getCurrentPool()).isEqualByComparingTo(new BigDecimal("5000.00"));

        // reward persisted
        List<JackpotReward> rewards = rewardRepository.findAll();
        assertThat(rewards).hasSize(1);
        JackpotReward saved = rewards.get(0);
        assertThat(saved.getBetId()).isEqualTo(2001L);
        assertThat(saved.getUserId()).isEqualTo(77L);
        assertThat(saved.getJackpotRewardAmount()).isEqualByComparingTo(new BigDecimal("1000000"));
    }
}