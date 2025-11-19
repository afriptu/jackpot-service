package org.example.assessmentjackpot.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "jackpot_rewards", uniqueConstraints = {
        @UniqueConstraint(name = "uc_reward_bet_id", columnNames = {"bet_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JackpotReward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bet_id", nullable = false)
    private Long betId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "jackpot_id", nullable = false)
    private Long jackpotId;

    @Column(precision = 19, scale = 2)
    private BigDecimal jackpotRewardAmount;

    private LocalDateTime createdAt;
}