package org.example.assessmentjackpot.service;

import org.example.assessmentjackpot.model.Jackpot;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component("variableContribution")
public class VariableContributionStrategy implements ContributionStrategy {
    // Start at 20%, decrease by 1% for every 1000 in the pool, down to a min of 5%
    private static final BigDecimal BASE_PERCENTAGE = new BigDecimal("0.20");
    private static final BigDecimal MIN_PERCENTAGE = new BigDecimal("0.05");
    private static final BigDecimal POOL_STEP = new BigDecimal("1000");
    private static final BigDecimal PERCENTAGE_DECREMENT = new BigDecimal("0.01");

    @Override
    public BigDecimal calculate(Jackpot jackpot, BigDecimal betAmount) {
        int steps = jackpot.getCurrentPool().divide(POOL_STEP, 0, RoundingMode.FLOOR).intValue();
        BigDecimal currentPercentage = BASE_PERCENTAGE.subtract(PERCENTAGE_DECREMENT.multiply(new BigDecimal(steps)));
        if (currentPercentage.compareTo(MIN_PERCENTAGE) < 0) {
            currentPercentage = MIN_PERCENTAGE;
        }
        return betAmount.multiply(currentPercentage).setScale(2, RoundingMode.HALF_UP);
    }
}