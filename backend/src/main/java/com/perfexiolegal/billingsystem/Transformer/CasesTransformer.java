package com.perfexiolegal.billingsystem.Transformer;

import com.perfexiolegal.billingsystem.Model.Cases;
import com.perfexiolegal.billingsystem.Model.CasesWithoutId;
import org.springframework.stereotype.Component;

@Component
public class CasesTransformer {

  public Cases update(CasesWithoutId updatedCase, String caseID) {
    return new Cases(
        caseID,
        updatedCase.getCaseName(),
        updatedCase.getClientId(),
        updatedCase.getCurrencyCode(),
        updatedCase.getDisbursementsAmount(),
        updatedCase.getServicesAmount(),
        updatedCase.getAmount()
    );
  }

  public Cases updateAmount(Cases updatedCase, double disbursementsAmount, double servicesAmount) {
    return new Cases(
        updatedCase.getCaseId(),
        updatedCase.getCaseName(),
        updatedCase.getClientId(),
        updatedCase.getCurrencyCode(),
        updatedCase.getDisbursementsAmount() + disbursementsAmount,
        updatedCase.getServicesAmount() + servicesAmount,
        updatedCase.getAmount() + disbursementsAmount + servicesAmount
    );
  }
}
