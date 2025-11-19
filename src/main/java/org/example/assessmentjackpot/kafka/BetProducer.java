package org.example.assessmentjackpot.kafka;

import org.example.assessmentjackpot.dto.BetDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BetProducer {

    private static final Logger log = LoggerFactory.getLogger(BetProducer.class);
    private final BetConsumer betConsumer;

    public BetProducer(BetConsumer betConsumer) {
        this.betConsumer = betConsumer;
    }

    public void sendBet(BetDto bet) {
        log.info("--- MOCKED KAFKA PRODUCER ---");
        log.info("Publishing bet to 'jackpot-bets' topic: {}", bet);

        // To simulate, we directly call the consumer's logic.
        betConsumer.processBet(bet);
        log.info("------------------------------");
    }
}