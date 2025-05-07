package com.perfexiolegal.billingsystem.Transformer;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
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
     * @throws ServiceException if the amounts are negative
     */
    public Cases updateAmount(Cases updatedCase, double disbursementsAmount, double servicesAmount) 
            throws ServiceException {
        if (disbursementsAmount < 0 || servicesAmount < 0) {
            logger.error("Attempted to add negative amounts: disbursements={}, services={}", 
                    disbursementsAmount, servicesAmount);
            throw new ServiceException("Amounts cannot be negative");
        }

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
