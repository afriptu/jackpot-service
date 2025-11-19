package org.example.assessmentjackpot.controller;

import org.example.assessmentjackpot.dto.JackpotEvaluationRequest;
import org.example.assessmentjackpot.dto.JackpotEvaluationResponse;
import org.example.assessmentjackpot.service.JackpotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jackpots")
public class JackpotController {
    private final JackpotService jackpotService;

    public JackpotController(JackpotService jackpotService) {
        this.jackpotService = jackpotService;
    }

    @PostMapping("/evaluate")
    public ResponseEntity<JackpotEvaluationResponse> evaluate(@RequestBody JackpotEvaluationRequest request) {
        var amount = jackpotService.evaluateWin(request);
        return ResponseEntity.ok(new JackpotEvaluationResponse(amount));
    }
}