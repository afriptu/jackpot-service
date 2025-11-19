package org.example.assessmentjackpot.service.strategy;

import org.example.assessmentjackpot.model.ContributionType;
import org.example.assessmentjackpot.model.Jackpot;
import org.example.assessmentjackpot.model.RewardType;
import org.example.assessmentjackpot.service.VariableContributionStrategy;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class VariableContributionStrategyTest {

    private final VariableContributionStrategy strategy = new VariableContributionStrategy();

    @Test
    void usesBasePercentageWhenPoolIsZero() {
        Jackpot jackpot = new Jackpot(1L, "test", new BigDecimal("1000.00"), ContributionType.VARIABLE, RewardType.FIXED);
        jackpot.setCurrentPool(new BigDecimal("0"));
        BigDecimal betAmount = new BigDecimal("100.00");

        BigDecimal contribution = strategy.calculate(jackpot, betAmount);

        // base percentage 20% -> 100 * 0.20 = 20.00
        assertThat(contribution).isEqualByComparingTo(new BigDecimal("20.00"));
    }

    @Test
    void decreasesPercentageAsPoolIncreases() {
        Jackpot jackpot = new Jackpot(2L, "test2", new BigDecimal("1000.00"), ContributionType.VARIABLE, RewardType.FIXED);
        // set currentPool to 2000 so steps = 2 -> decrement = 2 * 0.01 => 0.02
        // current percentage = 0.20 - 0.02 = 0.18
        jackpot.setCurrentPool(new BigDecimal("2000"));
        BigDecimal betAmount = new BigDecimal("100.00");

        BigDecimal contribution = strategy.calculate(jackpot, betAmount);

        // 100 * 0.18 = 18.00
        assertThat(contribution).isEqualByComparingTo(new BigDecimal("18.00"));
    }

    @Test
    void doesNotGoBelowMinimumPercentage() {
        Jackpot jackpot = new Jackpot(3L, "test3", new BigDecimal("1000.00"), ContributionType.VARIABLE, RewardType.FIXED);
        // make pool very large so percentage would go below MIN_PERCENTAGE
        jackpot.setCurrentPool(new BigDecimal("10000000"));
        BigDecimal betAmount = new BigDecimal("100.00");

        BigDecimal contribution = strategy.calculate(jackpot, betAmount);

        // minimum percentage is 5% -> 100 * 0.05 = 5.00
        assertThat(contribution).isEqualByComparingTo(new BigDecimal("5.00"));
    }
}