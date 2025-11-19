package org.example.assessmentjackpot.controller;

import org.example.assessmentjackpot.dto.BetDto;
import org.example.assessmentjackpot.service.BetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bets")
public class BetController {
    private final BetService betService;

    public BetController(BetService betService) {
        this.betService = betService;
    }

    @PostMapping
    public ResponseEntity<Void> publishBet(@RequestBody BetDto bet) {
        betService.publishBet(bet);
        return ResponseEntity.accepted().build();
    }
}