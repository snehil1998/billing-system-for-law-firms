package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.Cases;
import com.perfexiolegal.billingsystem.Repository.CasesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

/**
 * Service class for handling case-related business logic.
 * Acts as an intermediary between the controller and repository layers.
 */
@Service
public class CasesService {

    private static final Logger logger = LoggerFactory.getLogger(CasesService.class);

    @Autowired
    private CasesRepository casesRepository;

    /**
     * Retrieves all cases from the system.
     * @return Optional containing a list of all cases, or empty if none found
     * @throws ServiceException if there is an error retrieving the cases
     */
    public Optional<List<Cases>> getAllCases() throws ServiceException {
        try {
            logger.debug("Retrieving all cases");
            return casesRepository.getAllCases();
        } catch (RepositoryException e) {
            logger.error("Error retrieving all cases: {}", e.getMessage());
            throw new ServiceException("Failed to retrieve cases", e);
        }
    }

    /**
     * Retrieves a specific case by ID.
     * @param caseID The ID of the case to retrieve
     * @return Optional containing the case if found, empty otherwise
     * @throws ServiceException if there is an error retrieving the case
     */
    public Optional<Cases> getCaseById(String caseID) throws ServiceException {
        if (!StringUtils.hasText(caseID)) {
            throw new ServiceException("Case ID cannot be empty");
        }

        try {
            logger.debug("Retrieving case with ID: {}", caseID);
            return casesRepository.getCaseById(caseID);
        } catch (RepositoryException e) {
            logger.error("Error retrieving case with ID {}: {}", caseID, e.getMessage());
            throw new ServiceException("Failed to retrieve case", e);
        }
    }

    /**
     * Creates a new case in the system.
     * @param case_ The case to create
     * @return The created case
     * @throws ServiceException if there is an error creating the case or if validation fails
     */
    public Cases postCases(Cases case_) throws ServiceException {
        validateCase(case_);
        validateCaseDoesNotExist(case_.getCaseId());

        try {
            logger.debug("Creating new case with ID: {}", case_.getCaseId());
            return casesRepository.postCases(case_);
        } catch (RepositoryException e) {
            logger.error("Error creating case: {}", e.getMessage());
            throw new ServiceException("Failed to create case", e);
        }
    }

    /**
     * Updates an existing case in the system.
     * @param case_ The case data to update
     * @return The updated case
     * @throws ServiceException if there is an error updating the case or if validation fails
     */
    public Cases updateCase(Cases case_) throws ServiceException {
        validateCase(case_);
        validateCaseExists(case_.getCaseId());

        try {
            logger.debug("Updating case with ID: {}", case_.getCaseId());
            return casesRepository.updateCases(case_);
        } catch (RepositoryException e) {
            logger.error("Error updating case: {}", e.getMessage());
            throw new ServiceException("Failed to update case", e);
        }
    }

    /**
     * Deletes a case from the system.
     * @param caseID The ID of the case to delete
     * @return The number of rows affected (1 if successful, 0 if not found)
     * @throws ServiceException if there is an error deleting the case
     */
    public int deleteById(String caseID) throws ServiceException {
        if (!StringUtils.hasText(caseID)) {
            throw new ServiceException("Case ID cannot be empty");
        }

        validateCaseExists(caseID);

        try {
            logger.debug("Deleting case with ID: {}", caseID);
            return casesRepository.deleteById(caseID);
        } catch (RepositoryException e) {
            logger.error("Error deleting case: {}", e.getMessage());
            throw new ServiceException("Failed to delete case", e);
        }
    }

    /**
     * Updates the amounts for a case.
     * @param caseID The ID of the case to update
     * @param disbursementsAmount The amount to add to disbursements
     * @param servicesAmount The amount to add to services
     * @return The updated case
     * @throws ServiceException if there is an error updating the amounts
     */
    public Cases updateAmounts(String caseID, double disbursementsAmount, double servicesAmount) 
            throws ServiceException {
        if (!StringUtils.hasText(caseID)) {
            throw new ServiceException("Case ID cannot be empty");
        }

        if (disbursementsAmount < 0 || servicesAmount < 0) {
            throw new ServiceException("Amounts cannot be negative");
        }

        Optional<Cases> existingCase = getCaseById(caseID);
        if (existingCase.isEmpty()) {
            throw new ServiceException("Case not found with ID: " + caseID);
        }

        Cases case_ = existingCase.get();
        double newDisbursementsAmount = case_.getDisbursementsAmount() + disbursementsAmount;
        double newServicesAmount = case_.getServicesAmount() + servicesAmount;
        double newTotalAmount = newDisbursementsAmount + newServicesAmount;

        Cases updatedCase = Cases.builder()
                .caseId(case_.getCaseId())
                .caseName(case_.getCaseName())
                .clientId(case_.getClientId())
                .currencyCode(case_.getCurrencyCode())
                .disbursementsAmount(newDisbursementsAmount)
                .servicesAmount(newServicesAmount)
                .amount(newTotalAmount)
                .build();

        return updateCase(updatedCase);
    }

    /**
     * Validates a case object.
     * @param case_ The case to validate
     * @throws ServiceException if validation fails
     */
    private void validateCase(Cases case_) throws ServiceException {
        if (case_ == null) {
            throw new ServiceException("Case cannot be null");
        }

        if (!StringUtils.hasText(case_.getCaseId())) {
            throw new ServiceException("Case ID cannot be empty");
        }

        if (!StringUtils.hasText(case_.getCaseName())) {
            throw new ServiceException("Case name cannot be empty");
        }

        if (!StringUtils.hasText(case_.getClientId())) {
            throw new ServiceException("Client ID cannot be empty");
        }

        if (!StringUtils.hasText(case_.getCurrencyCode())) {
            throw new ServiceException("Currency code cannot be empty");
        }

        try {
            Currency.getInstance(case_.getCurrencyCode());
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Invalid currency code: " + case_.getCurrencyCode());
        }

        if (case_.getDisbursementsAmount() < 0 || case_.getServicesAmount() < 0 || case_.getAmount() < 0) {
            throw new ServiceException("Amounts cannot be negative");
        }

        // Validate that total amount matches sum of disbursements and services
        double expectedTotal = case_.getDisbursementsAmount() + case_.getServicesAmount();
        if (Math.abs(case_.getAmount() - expectedTotal) > 0.001) {
            throw new ServiceException("Total amount must equal sum of disbursements and services amounts");
        }
    }

    /**
     * Validates that a case exists.
     * @param caseID The ID of the case to validate
     * @throws ServiceException if the case does not exist
     */
    private void validateCaseExists(String caseID) throws ServiceException {
        Optional<Cases> case_ = getCaseById(caseID);
        if (case_.isEmpty()) {
            throw new ServiceException("Case not found with ID: " + caseID);
        }
    }

    /**
     * Validates that a case does not exist.
     * @param caseID The ID of the case to validate
     * @throws ServiceException if the case already exists
     */
    private void validateCaseDoesNotExist(String caseID) throws ServiceException {
        Optional<Cases> case_ = getCaseById(caseID);
        if (case_.isPresent()) {
            throw new ServiceException("Case already exists with ID: " + caseID);
        }
    }
}
