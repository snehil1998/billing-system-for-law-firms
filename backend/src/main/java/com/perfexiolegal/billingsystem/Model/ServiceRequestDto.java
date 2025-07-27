package com.perfexiolegal.billingsystem.Model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@SuperBuilder
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ServiceRequestDto {
    public ServiceRequestDto() {}
    private String caseId;
    private String clientId;
    private String service;
    private String description;
    private Date date;
    private List<AttorneyMinutes> attorneys;
    private double amount;
} 