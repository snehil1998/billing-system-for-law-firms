package com.perfexiolegal.billingsystem.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CasesWithoutId {
  private String caseName;
  private String clientId;
  private String currencyCode;
  private double disbursementsAmount;
  private double servicesAmount;
  private float amount;
}
