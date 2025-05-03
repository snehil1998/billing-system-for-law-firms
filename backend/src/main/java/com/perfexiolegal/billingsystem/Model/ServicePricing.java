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
public class   ServicePricing {
  private String clientId;
  private float price;
  public ServicePricing() {

  }
}
