package com.perfexiolegal.billingsystem.Transformer;

import com.perfexiolegal.billingsystem.Model.Cases;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Transformer class for handling case-related data transformations.
 * Converts between different case data models and handles amount calculations.
 */
@Component
public class CasesTransformer {

    private static final Logger logger = LoggerFactory.getLogger(CasesTransformer.class);

    /**
     * Updates the amounts for a case by adding new amounts to existing ones.
     * @param updatedCase The case to update
     * @param disbursementsAmount The amount to add to disbursements
     * @param servicesAmount The amount to add to services
     * @return The updated case with new amounts
     */
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
