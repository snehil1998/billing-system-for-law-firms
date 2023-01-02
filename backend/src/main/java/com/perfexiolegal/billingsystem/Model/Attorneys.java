package com.perfexiolegal.billingsystem.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Attorneys {
  private String attorneyId;
  private String firstName;
  private String lastName;
  private List<ServicePricing> servicePricing;
}
