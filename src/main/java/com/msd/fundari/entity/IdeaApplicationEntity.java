package com.msd.fundari.entity;

import com.msd.fundari.utils.enums.IdeaTarget;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "idea_application_entity")
public class IdeaApplicationEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "application_id", nullable = false)
  private ApplicationEntity application;

  @Column(nullable = false)
  private String description;

  @Enumerated(EnumType.STRING)
  private IdeaTarget target;

  private Boolean hasTeam;

  private Boolean isConceptOnly;

  private Boolean isThereSimilarProducts;

  private Boolean hasInvestors;
}
