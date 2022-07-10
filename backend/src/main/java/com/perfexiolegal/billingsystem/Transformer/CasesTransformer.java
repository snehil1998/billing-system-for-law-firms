package com.perfexiolegal.billingsystem.Transformer;

import com.perfexiolegal.billingsystem.Model.Cases;
import com.perfexiolegal.billingsystem.Model.CasesWithoutId;
import com.perfexiolegal.billingsystem.Model.Services;
import com.perfexiolegal.billingsystem.Model.ServicesWithoutId;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CasesTransformer {

  public Cases update(CasesWithoutId updatedCase, UUID caseID) {
    return new Cases(
        caseID,
        updatedCase.getCaseName(),
        updatedCase.getCurrencyCode(),
        updatedCase.getAmount()
    );
  }
}
