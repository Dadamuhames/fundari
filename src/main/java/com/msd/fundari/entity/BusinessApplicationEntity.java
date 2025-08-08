package com.msd.fundari.entity;

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
@Table(name = "business_application_entity")
public class BusinessApplicationEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String industry;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "application_id", nullable = false)
  private ApplicationEntity application;

  @Column(nullable = false)
  private String businessAge;

  @Column(nullable = false)
  private BigDecimal avgMonthlyProfit;

  @Column(nullable = false)
  private BigDecimal netProfit;

  private BigDecimal estimateValueOfAssets;

  @Column(nullable = false)
  private Integer teamSize;

  private boolean hasAssets;

  private boolean hasDebtsOrLoans;

  @Column(nullable = false)
  private String regionOfActivity;
}
