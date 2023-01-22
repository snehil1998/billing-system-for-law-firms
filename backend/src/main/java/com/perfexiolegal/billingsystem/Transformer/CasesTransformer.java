package com.perfexiolegal.billingsystem.Transformer;

import com.perfexiolegal.billingsystem.Model.Cases;
import com.perfexiolegal.billingsystem.Model.CasesWithoutId;
import com.perfexiolegal.billingsystem.Model.Services;
import com.perfexiolegal.billingsystem.Model.ServicesWithoutId;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CasesTransformer {

  public Cases update(CasesWithoutId updatedCase, String caseID) {
    return new Cases(
        caseID,
        updatedCase.getCaseName(),
        updatedCase.getClientId(),
        updatedCase.getCurrencyCode(),
        updatedCase.getAmount()
    );
  }

  public Cases updateAmount(Cases updatedCase, double amount) {
    return new Cases(
        updatedCase.getCaseId(),
        updatedCase.getCaseName(),
        updatedCase.getClientId(),
        updatedCase.getCurrencyCode(),
        updatedCase.getAmount() + amount
    );
  }
}
