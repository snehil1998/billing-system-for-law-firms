package com.perfexiolegal.billingsystem.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.json.simple.JSONArray;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ServicesWithoutId {
  private UUID caseId;
  private UUID clientId;
  private String service;
  private String description;
  private Date date;
  private List<AttorneysInService> attorneys;
  private double amount;
}
