package org.example.assessmentjackpot.kafka;

import org.example.assessmentjackpot.dto.BetDto;
import org.example.assessmentjackpot.service.JackpotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BetConsumer {
    private static final Logger log = LoggerFactory.getLogger(BetConsumer.class);
    private final JackpotService jackpotService;

    public BetConsumer(JackpotService jackpotService) {
        this.jackpotService = jackpotService;
    }

    // This annotation would be active in a real Kafka setup.
    // @KafkaListener(topics = "jackpot-bets", groupId = "jackpot-group")
    public void listen(BetDto bet) {
        processBet(bet);
    }

    public void processBet(BetDto bet) {
        log.info("--- MOCKED KAFKA CONSUMER ---");
        log.info("Consumed bet from 'jackpot-bets' topic: {}", bet);
        jackpotService.processContribution(bet);
        log.info("------------------------------");
    }
}