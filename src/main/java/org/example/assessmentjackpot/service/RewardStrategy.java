package org.example.assessmentjackpot.service;

import org.example.assessmentjackpot.model.Jackpot;

public interface RewardStrategy {
    boolean isWin(Jackpot jackpot);
}