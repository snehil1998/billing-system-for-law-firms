package com.perfexiolegal.billingsystem.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Disbursements {
  private String disbursementId;
  private String caseId;
  private String clientId;
  private String disbursement;
  private Date date;
  private String currencyCode;
  private double conversionRate;
  private double inrAmount;
  private double conversionAmount;
}
