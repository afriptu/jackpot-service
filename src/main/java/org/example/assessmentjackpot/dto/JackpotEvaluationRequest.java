package org.example.assessmentjackpot.dto;

import lombok.Data;

@Data
public class JackpotEvaluationRequest {
    private Long betId;
    private Long userId;
    private Long jackpotId;
}