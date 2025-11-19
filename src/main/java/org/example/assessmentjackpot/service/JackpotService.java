package org.example.assessmentjackpot.service;

import lombok.extern.slf4j.Slf4j;
import org.example.assessmentjackpot.dto.BetDto;
import org.example.assessmentjackpot.dto.JackpotEvaluationRequest;
import org.example.assessmentjackpot.model.*;
import org.example.assessmentjackpot.repository.JackpotContributionRepository;
import org.example.assessmentjackpot.repository.JackpotRepository;
import org.example.assessmentjackpot.repository.JackpotRewardRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
public class JackpotService {
    private final JackpotRepository jackpotRepository;
    private final JackpotContributionRepository contributionRepository;
    private final JackpotRewardRepository rewardRepository;
    private final ApplicationContext ctx;

    public JackpotService(JackpotRepository jackpotRepository,
                          JackpotContributionRepository contributionRepository,
                          JackpotRewardRepository rewardRepository,
                          ApplicationContext ctx) {
        this.jackpotRepository = jackpotRepository;
        this.contributionRepository = contributionRepository;
        this.rewardRepository = rewardRepository;
        this.ctx = ctx;
    }

    @Transactional
    @Retryable(
            value = OptimisticLockingFailureException.class,
            maxAttempts = 5,  // Retry up to 5 times
            backoff = @Backoff(
                    delay = 50,      // Start with 50ms
                    multiplier = 2,  // Double each time: 50ms, 100ms, 200ms, 400ms, 800ms
                    maxDelay = 1000  // Cap at 1 second
            )
    )
    public void processContribution(BetDto bet) {
        if (contributionRepository.existsByBetId(bet.getBetId())) {
            log.info("Contribution for bet {} already exists; skipping (idempotent)", bet.getBetId());
            return;
        }
        Jackpot jackpot = jackpotRepository.findById(bet.getJackpotId())
                .orElseThrow(() -> new IllegalArgumentException("Jackpot not found with ID: " + bet.getJackpotId()));

        String beanName = contributionBeanName(jackpot.getContributionType());
        ContributionStrategy strat = ctx.getBean(beanName, ContributionStrategy.class);

        BigDecimal contributionAmount = strat.calculate(jackpot, bet.getBetAmount());
        jackpot.setCurrentPool(jackpot.getCurrentPool().add(contributionAmount));
        jackpotRepository.save(jackpot);

        JackpotContribution contribution = JackpotContribution.builder()
                .betId(bet.getBetId())
                .userId(bet.getUserId())
                .jackpotId(bet.getJackpotId())
                .stakeAmount(bet.getBetAmount())
                .contributionAmount(contributionAmount)
                .currentJackpotAmount(jackpot.getCurrentPool())
                .createdAt(LocalDateTime.now())
                .build();
        contributionRepository.save(contribution);

        log.info("Contribution processed: {} added to jackpot {}", contributionAmount, bet.getJackpotId());

    }

    @Transactional
    @Retryable(
            value = OptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 100, multiplier = 2)
    )
    public BigDecimal evaluateWin(JackpotEvaluationRequest request) {
        Jackpot jackpot = jackpotRepository.findById(request.getJackpotId())
                .orElseThrow(() -> new IllegalArgumentException("Jackpot not found with ID: " + request.getJackpotId()));

        if (rewardRepository.existsByBetId(request.getBetId())) {
            log.info("Reward for bet {} already exists; returning existing reward", request.getBetId());
            JackpotReward existing = rewardRepository.findAll().stream()
                    .filter(r -> r.getBetId().equals(request.getBetId()))
                    .findFirst()
                    .orElse(null);
            return existing != null ? existing.getJackpotRewardAmount() : BigDecimal.ZERO;
        }

        String beanName = rewardBeanName(jackpot.getRewardType());
        RewardStrategy rewardStrategy = ctx.getBean(beanName, RewardStrategy.class);

        boolean isWin = rewardStrategy.isWin(jackpot);
        if (isWin) {
            BigDecimal rewardAmount = jackpot.getCurrentPool();
            // reset
            jackpot.resetToInitial();
            jackpotRepository.save(jackpot);

            JackpotReward reward = JackpotReward.builder()
                    .betId(request.getBetId())
                    .userId(request.getUserId())
                    .jackpotId(request.getJackpotId())
                    .jackpotRewardAmount(rewardAmount)
                    .createdAt(LocalDateTime.now())
                    .build();
            rewardRepository.save(reward);
            return rewardAmount;
        }
        return BigDecimal.ZERO;
    }

    private String contributionBeanName(ContributionType type) {
        return switch (type) {
            case FIXED -> "fixedContribution";
            case VARIABLE -> "variableContribution";
        };
    }

    private String rewardBeanName(RewardType type) {
        return switch (type) {
            case FIXED -> "fixedReward";
            case VARIABLE -> "variableReward";
        };
    }
}