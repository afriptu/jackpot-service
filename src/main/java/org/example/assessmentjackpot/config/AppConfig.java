package org.example.assessmentjackpot.config;

import org.example.assessmentjackpot.model.ContributionType;
import org.example.assessmentjackpot.model.Jackpot;
import org.example.assessmentjackpot.model.RewardType;
import org.example.assessmentjackpot.repository.JackpotRepository;
import org.example.assessmentjackpot.service.ContributionStrategy;
import org.example.assessmentjackpot.service.RewardStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class AppConfig {
    @Bean
    CommandLineRunner init(JackpotRepository jackpotRepository) {
        return args -> {
            // seed two jackpots
            Jackpot j1 = new Jackpot(1L, "Global Jackpot", new BigDecimal("10000.00"),
                    ContributionType.FIXED, RewardType.VARIABLE);
            Jackpot j2 = new Jackpot(2L, "Local Jackpot", new BigDecimal("500.00"),
                    ContributionType.VARIABLE, RewardType.FIXED);

            jackpotRepository.save(j1);
            jackpotRepository.save(j2);
        };
    }
}