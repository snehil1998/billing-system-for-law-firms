package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.DisbursementDetails;
import com.perfexiolegal.billingsystem.Repository.DisbursementsRepository;
import com.perfexiolegal.billingsystem.Repository.ServicesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class DisbursementsService {

    private static final Logger logger = LoggerFactory.getLogger(DisbursementsService.class);

    @Autowired
    private DisbursementsRepository disbursementsRepository;

    @Autowired
    private CasesService casesService;

    @Autowired
    private ClientsService clientsService;

    public Optional<List<DisbursementDetails>> getAllDisbursements() throws ServiceException {
        try {
            logger.debug("Retrieving all disbursements");
            return disbursementsRepository.getAllDisbursements();
        } catch (RepositoryException e) {
            logger.error("Error retrieving all disbursements: {}", e.getMessage());
            throw new ServiceException("Failed to retrieve disbursements", e);
        }
    }

    public Optional<DisbursementDetails> getDisbursementsById(String disbursementId) throws ServiceException {
        try {
            validateDisbursementId(disbursementId);
            logger.debug("Retrieving disbursement with ID: {}", disbursementId);
            return disbursementsRepository.getDisbursementsById(disbursementId);
        } catch (RepositoryException e) {
            logger.error("Error retrieving disbursement with ID {}: {}", disbursementId, e.getMessage());
            throw new ServiceException("Failed to retrieve disbursement", e);
        }
    }

    public Optional<List<DisbursementDetails>> getDisbursementsByClientId(String clientId) throws ServiceException {
        try {
            validateClientId(clientId);
            logger.debug("Retrieving disbursements for client with ID: {}", clientId);
            return disbursementsRepository.getDisbursementsByClientId(clientId);
        } catch (RepositoryException e) {
            logger.error("Error retrieving disbursements for client {}: {}", clientId, e.getMessage());
            throw new ServiceException("Failed to retrieve disbursements for client", e);
        }
    }

    public Optional<List<DisbursementDetails>> getDisbursementsByCaseId(String caseId) throws ServiceException {
        try {
            validateCaseId(caseId);
            logger.debug("Retrieving disbursements for case with ID: {}", caseId);
            return disbursementsRepository.getDisbursementsByCaseId(caseId);
        } catch (RepositoryException e) {
            logger.error("Error retrieving disbursements for case {}: {}", caseId, e.getMessage());
            throw new ServiceException("Failed to retrieve disbursements for case", e);
        }
    }

    public void postDisbursements(DisbursementDetails disbursement) throws ServiceException {
        try {
            validateDisbursement(disbursement);
            logger.debug("Creating disbursement with ID: {}", disbursement.getDisbursementId());
            
            casesService.updateAmounts(disbursement.getCaseId(), disbursement.getConversionAmount(), 0);
            clientsService.updateAmounts(disbursement.getClientId(), disbursement.getConversionAmount(), 0);
            disbursementsRepository.postDisbursements(disbursement);
        } catch (RepositoryException e) {
            logger.error("Error creating disbursement: {}", e.getMessage());
            throw new ServiceException("Failed to create disbursement", e);
        }
    }

    public void updateDisbursements(DisbursementDetails disbursement) throws ServiceException {
        try {
            validateDisbursement(disbursement);
            validateDisbursementExists(disbursement.getDisbursementId());
            logger.debug("Updating disbursement with ID: {}", disbursement.getDisbursementId());
            disbursementsRepository.updateDisbursements(disbursement);
        } catch (RepositoryException e) {
            logger.error("Error updating disbursement: {}", e.getMessage());
            throw new ServiceException("Failed to update disbursement", e);
        }
    }

    public int deleteDisbursementById(String disbursementId) throws ServiceException {
        try {
            validateDisbursementId(disbursementId);
            validateDisbursementExists(disbursementId);
            logger.debug("Deleting disbursement with ID: {}", disbursementId);
            
            DisbursementDetails disbursement = getDisbursementsById(disbursementId)
                    .orElseThrow(() -> new ServiceException("Disbursement not found with ID: " + disbursementId));
            
            casesService.updateAmounts(disbursement.getCaseId(), -disbursement.getConversionAmount(), 0);
            clientsService.updateAmounts(disbursement.getClientId(), -disbursement.getConversionAmount(), 0);
            return disbursementsRepository.deleteDisbursementById(disbursementId);
        } catch (RepositoryException e) {
            logger.error("Error deleting disbursement: {}", e.getMessage());
            throw new ServiceException("Failed to delete disbursement", e);
        }
    }

    private void validateDisbursementExists(String disbursementId) throws ServiceException {
      Optional<DisbursementDetails> disbursement = getDisbursementsById(disbursementId);
      if (disbursement.isEmpty()) {
          throw new ServiceException("Disbursement not found with ID: " + disbursementId);
      }
    }

    private void validateDisbursementId(String disbursementId) throws ServiceException {
        if (!StringUtils.hasText(disbursementId)) {
            throw new ServiceException("Disbursement ID cannot be empty");
        }
    }

    private void validateClientId(String clientId) throws ServiceException {
        if (!StringUtils.hasText(clientId)) {
            throw new ServiceException("Client ID cannot be empty");
        }
    }

    private void validateCaseId(String caseId) throws ServiceException {
        if (!StringUtils.hasText(caseId)) {
            throw new ServiceException("Case ID cannot be empty");
        }
    }

    private void validateDisbursement(DisbursementDetails disbursement) throws ServiceException {
        if (disbursement == null) {
            throw new ServiceException("Disbursement cannot be null");
        }
        validateDisbursementId(disbursement.getDisbursementId());
        validateClientId(disbursement.getClientId());
        validateCaseId(disbursement.getCaseId());
        
        if (!StringUtils.hasText(disbursement.getDisbursement())) {
            throw new ServiceException("Disbursement description cannot be empty");
        }
        if (!StringUtils.hasText(disbursement.getCurrencyCode())) {
            throw new ServiceException("Currency code cannot be empty");
        }
        if (disbursement.getDate() == null) {
            throw new ServiceException("Date cannot be null");
        }
        if (disbursement.getConversionRate() <= 0) {
            throw new ServiceException("Conversion rate must be positive");
        }
        if (disbursement.getInrAmount() < 0) {
            throw new ServiceException("INR amount cannot be negative");
        }
        if (disbursement.getConversionAmount() < 0) {
            throw new ServiceException("Conversion amount cannot be negative");
        }
    }
}
