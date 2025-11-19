package org.example.assessmentjackpot.service;

import org.example.assessmentjackpot.model.Jackpot;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component("fixedContribution")
public class FixedContributionStrategy implements ContributionStrategy {
    private static final BigDecimal PERCENTAGE = new BigDecimal("0.10"); // 10%

    @Override
    public BigDecimal calculate(Jackpot jackpot, BigDecimal betAmount) {
        return betAmount.multiply(PERCENTAGE).setScale(2, RoundingMode.HALF_UP);
    }
}