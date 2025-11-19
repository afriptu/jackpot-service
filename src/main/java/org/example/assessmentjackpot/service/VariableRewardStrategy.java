package org.example.assessmentjackpot.service;

import org.example.assessmentjackpot.model.Jackpot;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Component("variableReward")
public class VariableRewardStrategy implements RewardStrategy {
    // Start at 1% chance, increase by 0.5% for every 1000 in the pool, up to a max of 50%
    // If pool hits 1,000,000, chance is 100%
    private static final double BASE_CHANCE = 0.01;
    private static final double MAX_CHANCE = 0.50;
    private static final BigDecimal POOL_LIMIT = new BigDecimal("1000000");
    private static final BigDecimal POOL_STEP = new BigDecimal("1000");
    private static final double CHANCE_INCREMENT = 0.005;

    @Override
    public boolean isWin(Jackpot jackpot) {
        if (jackpot.getCurrentPool().compareTo(POOL_LIMIT) >= 0) {
            return true; // 100% chance if limit is hit
        }

        int steps = jackpot.getCurrentPool().divide(POOL_STEP).intValue();
        double currentChance = BASE_CHANCE + (steps * CHANCE_INCREMENT);
        if (currentChance > MAX_CHANCE) {
            currentChance = MAX_CHANCE;
        }
        return ThreadLocalRandom.current().nextDouble() < currentChance;
    }
}