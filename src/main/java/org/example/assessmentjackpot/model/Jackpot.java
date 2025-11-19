package org.example.assessmentjackpot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "jackpots")
@Getter
@Setter
@NoArgsConstructor
public class Jackpot {
    @Id
    private Long id;

    private String name;

    @Column(name = "initial_pool", precision = 19, scale = 2)
    private BigDecimal initialPool;

    @Column(name = "current_pool", precision = 19, scale = 2)
    private BigDecimal currentPool;

    @Enumerated(EnumType.STRING)
    @Column(name = "contribution_type")
    private ContributionType contributionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "reward_type")
    private RewardType rewardType;

    @Version
    private Long version;

    public Jackpot(Long id, String name, BigDecimal initialPool, ContributionType contributionType, RewardType rewardType) {
        this.id = id;
        this.name = name;
        this.initialPool = initialPool;
        this.currentPool = initialPool;
        this.contributionType = contributionType;
        this.rewardType = rewardType;
    }

    public void resetToInitial() {
        this.currentPool = this.initialPool;
    }
}