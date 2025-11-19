package org.example.assessmentjackpot.service;

import org.example.assessmentjackpot.model.Jackpot;

import java.math.BigDecimal;

public interface ContributionStrategy {
    BigDecimal calculate(Jackpot jackpot, BigDecimal betAmount);
}