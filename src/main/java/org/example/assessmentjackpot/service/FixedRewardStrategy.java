package org.example.assessmentjackpot.service;

import org.example.assessmentjackpot.model.Jackpot;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component("fixedReward")
public class FixedRewardStrategy implements RewardStrategy {
    private static final double WIN_CHANCE = 0.05; // 5%

    @Override
    public boolean isWin(Jackpot jackpot) {
        return ThreadLocalRandom.current().nextDouble() < WIN_CHANCE;
    }
}