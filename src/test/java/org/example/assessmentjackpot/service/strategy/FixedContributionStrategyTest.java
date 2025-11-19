package org.example.assessmentjackpot.service.strategy;

import org.example.assessmentjackpot.model.ContributionType;
import org.example.assessmentjackpot.model.Jackpot;
import org.example.assessmentjackpot.model.RewardType;
import org.example.assessmentjackpot.service.FixedContributionStrategy;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class FixedContributionStrategyTest {

    private final FixedContributionStrategy strategy = new FixedContributionStrategy();

    @Test
    void calculatesTenPercentOfBetAmount() {
        Jackpot jackpot = new Jackpot(1L, "test", new BigDecimal("1000.00"), ContributionType.FIXED, RewardType.FIXED);
        BigDecimal betAmount = new BigDecimal("123.45");

        BigDecimal contribution = strategy.calculate(jackpot, betAmount);

        // 10% of 123.45 = 12.345 -> rounded to 2 decimals HALF_UP => 12.35
        assertThat(contribution).isEqualByComparingTo(new BigDecimal("12.35"));
    }
}