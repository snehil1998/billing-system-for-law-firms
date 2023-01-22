package com.perfexiolegal.billingsystem.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ClientsWithoutId {
  private String clientName;
  private String currencyCode;
  private double disbursementsAmount;
  private double servicesAmount;
  private float amount;
}
