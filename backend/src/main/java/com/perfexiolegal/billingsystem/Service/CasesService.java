package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.Cases;
import com.perfexiolegal.billingsystem.Repository.CasesRepository;
import com.perfexiolegal.billingsystem.Transformer.CasesTransformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

@Service
public class CasesService {

    private static final Logger logger = LoggerFactory.getLogger(CasesService.class);

    @Autowired
    private CasesRepository casesRepository;

    @Autowired
    private CasesTransformer casesTransformer;

    public Optional<List<Cases>> getAllCases() throws ServiceException {
        try {
            logger.debug("Retrieving all cases");
            return casesRepository.getAllCases();
        } catch (RepositoryException e) {
            logger.error("Error retrieving all cases: {}", e.getMessage());
            throw new ServiceException("Failed to retrieve cases", e);
        }
    }

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

    public Cases updateAmounts(String caseID, double disbursementsAmount, double servicesAmount) 
            throws ServiceException {
        if (!StringUtils.hasText(caseID)) {
            throw new ServiceException("Case ID cannot be empty");
        }

        Optional<Cases> existingCase = getCaseById(caseID);
        if (existingCase.isEmpty()) {
            throw new ServiceException("Case not found with ID: " + caseID);
        }

        Cases updatedCase = casesTransformer.updateAmount(existingCase.get(), disbursementsAmount, servicesAmount);
        return updateCase(updatedCase);
    }

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

        // Validate that total amount matches sum of disbursements and services
        double expectedTotal = case_.getDisbursementsAmount() + case_.getServicesAmount();
        if (Math.abs(case_.getAmount() - expectedTotal) > 0.001) {
            throw new ServiceException("Total amount must equal sum of disbursements and services amounts");
        }
    }

    private void validateCaseExists(String caseID) throws ServiceException {
        Optional<Cases> case_ = getCaseById(caseID);
        if (case_.isEmpty()) {
            throw new ServiceException("Case not found with ID: " + caseID);
        }
    }

    private void validateCaseDoesNotExist(String caseID) throws ServiceException {
        Optional<Cases> case_ = getCaseById(caseID);
        if (case_.isPresent()) {
            throw new ServiceException("Case already exists with ID: " + caseID);
        }
    }
}
