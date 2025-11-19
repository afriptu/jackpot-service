package org.example.assessmentjackpot.service.strategy;

import org.example.assessmentjackpot.model.ContributionType;
import org.example.assessmentjackpot.model.Jackpot;
import org.example.assessmentjackpot.model.RewardType;
import org.example.assessmentjackpot.service.VariableRewardStrategy;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class VariableRewardStrategyTest {

    private final VariableRewardStrategy strategy = new VariableRewardStrategy();

    @Test
    void alwaysWinsWhenPoolHitsLimit() {
        Jackpot jackpot = new Jackpot(1L, "big", new BigDecimal("5000.00"), ContributionType.FIXED, RewardType.VARIABLE);
        // ensure we hit the deterministic branch: POOL_LIMIT is 1,000,000
        jackpot.setCurrentPool(new BigDecimal("1000000"));

        boolean win = strategy.isWin(jackpot);

        assertThat(win).isTrue();
    }
}