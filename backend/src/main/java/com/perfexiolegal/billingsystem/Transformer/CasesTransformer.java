package com.perfexiolegal.billingsystem.Transformer;

import com.perfexiolegal.billingsystem.Model.Cases;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CasesTransformer {

    private static final Logger logger = LoggerFactory.getLogger(CasesTransformer.class);

    public Cases updateAmount(Cases updatedCase, double disbursementsAmount, double servicesAmount) {
        logger.debug("Updating amounts for case with ID: {}", updatedCase.getCaseId());
        return Cases.builder()
                .caseId(updatedCase.getCaseId())
                .caseName(updatedCase.getCaseName())
                .clientId(updatedCase.getClientId())
                .currencyCode(updatedCase.getCurrencyCode())
                .disbursementsAmount(updatedCase.getDisbursementsAmount() + disbursementsAmount)
                .servicesAmount(updatedCase.getServicesAmount() + servicesAmount)
                .amount(updatedCase.getAmount() + disbursementsAmount + servicesAmount)
                .build();
    }
}
