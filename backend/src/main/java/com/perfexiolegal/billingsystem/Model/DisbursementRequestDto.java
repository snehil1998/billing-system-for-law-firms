package com.perfexiolegal.billingsystem.Model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@SuperBuilder
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DisbursementRequestDto {
    public DisbursementRequestDto() {}
    private String caseId;
    private String clientId;
    private String disbursement;
    private String description;
    private Date date;
    private String currencyCode;
    private double conversionRate;
    private double inrAmount;
    private double conversionAmount;
} 