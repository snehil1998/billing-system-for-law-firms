package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.CaseDetails;
import com.perfexiolegal.billingsystem.Repository.ICasesRepository;
import com.perfexiolegal.billingsystem.Transformer.CasesTransformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class CasesService implements ICasesService {

    private static final Logger logger = LoggerFactory.getLogger(CasesService.class);

    @Autowired
    private ICasesRepository casesRepository;

    @Autowired
    private CasesTransformer casesTransformer;

    public Optional<List<CaseDetails>> getAll() throws ServiceException {
        try {
            logger.debug("Retrieving all cases");
            return casesRepository.getAll();
        } catch (RepositoryException e) {
            logger.error("Error retrieving all cases: {}", e.getMessage());
            throw new ServiceException("Failed to retrieve cases", e);
        }
    }

    public Optional<CaseDetails> getById(String caseID) throws ServiceException {
        if (!StringUtils.hasText(caseID)) {
            throw new ServiceException("Case ID cannot be empty");
        }

        try {
            logger.debug("Retrieving case with ID: {}", caseID);
            return casesRepository.getById(caseID);
        } catch (RepositoryException e) {
            logger.error("Error retrieving case with ID {}: {}", caseID, e.getMessage());
            throw new ServiceException("Failed to retrieve case", e);
        }
    }

    public void create(CaseDetails case_) throws ServiceException {
        try {
            logger.debug("Creating new case with ID: {}", case_.getCaseId());
            casesRepository.create(case_);
        } catch (RepositoryException e) {
            logger.error("Error creating case: {}", e.getMessage());
            throw new ServiceException("Failed to create case", e);
        }
    }

    public void update(CaseDetails case_) throws ServiceException {
        try {
            logger.debug("Updating case with ID: {}", case_.getCaseId());
            casesRepository.update(case_);
        } catch (RepositoryException e) {
            logger.error("Error updating case: {}", e.getMessage());
            throw new ServiceException("Failed to update case", e);
        }
    }

    public int deleteById(String caseID) throws ServiceException {
        if (!StringUtils.hasText(caseID)) {
            throw new ServiceException("Case ID cannot be empty");
        }

        try {
            logger.debug("Deleting case with ID: {}", caseID);
            return casesRepository.deleteById(caseID);
        } catch (RepositoryException e) {
            logger.error("Error deleting case: {}", e.getMessage());
            throw new ServiceException("Failed to delete case", e);
        }
    }

    public CaseDetails updateAmounts(String caseID, double disbursementsAmount, double servicesAmount) 
            throws ServiceException {
        if (!StringUtils.hasText(caseID)) {
            throw new ServiceException("Case ID cannot be empty");
        }

        Optional<CaseDetails> existingCase = getById(caseID);
        if (existingCase.isEmpty()) {
            throw new ServiceException("Case not found with ID: " + caseID);
        }

        CaseDetails updatedCase = casesTransformer.updateAmount(existingCase.get(), disbursementsAmount, servicesAmount);
        update(updatedCase);
        return updatedCase;
    }
}
