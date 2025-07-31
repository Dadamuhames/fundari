package com.msd.fundari.entity;

import com.msd.fundari.utils.enums.StartupStage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "startup_application_entity")
public class StartupApplicationEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "application_id", nullable = false)
  private ApplicationEntity application;

  @Enumerated(EnumType.STRING)
  private StartupStage stage;

  @Column(nullable = false)
  private String description;

  private BigDecimal lastMonthRevenue;

  @Column(nullable = false)
  private Integer activeUserCount;

  private BigDecimal investedMoneyAmount;

  @Column(nullable = false)
  private Integer teamSize;

  private String competitors;

  @Column(nullable = false)
  private String regionOfActivity;
}
