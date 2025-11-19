package org.example.assessmentjackpot.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BetDto {
    private Long betId;
    private Long userId;
    private Long jackpotId;
    private BigDecimal betAmount;
}