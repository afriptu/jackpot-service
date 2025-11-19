package org.example.assessmentjackpot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JackpotEvaluationResponse {
    private BigDecimal rewardAmount;
}