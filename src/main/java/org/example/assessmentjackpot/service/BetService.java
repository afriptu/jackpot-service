package org.example.assessmentjackpot.service;

import org.example.assessmentjackpot.dto.BetDto;
import org.example.assessmentjackpot.kafka.BetProducer;
import org.springframework.stereotype.Service;

@Service
public class BetService {

    private final BetProducer betProducer;

    public BetService(BetProducer betProducer) {
        this.betProducer = betProducer;
    }

    public void publishBet(BetDto bet) {
        betProducer.sendBet(bet);
    }
}