package com.perfexiolegal.billingsystem.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ServiceResponse {
  private Long serviceId;
  private String caseId;
  private String clientId;
  private String service;
  private String description;
  private Date date;
  private List<AttorneyMinutes> attorneys;
  private double amount;
}
