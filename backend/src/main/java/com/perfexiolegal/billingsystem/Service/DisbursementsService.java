package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.Disbursements;
import com.perfexiolegal.billingsystem.Repository.DisbursementsRepository;
import com.perfexiolegal.billingsystem.Repository.ServicesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * Service class for handling disbursement-related business logic.
 * Provides methods for managing disbursements and their associated data.
 */
@Service
public class DisbursementsService {

    private static final Logger logger = LoggerFactory.getLogger(DisbursementsService.class);

    @Autowired
    private DisbursementsRepository disbursementsRepository;

    @Autowired
    private CasesService casesService;

    @Autowired
    private ClientsService clientsService;

    /**
     * Retrieves all disbursements.
     * @return Optional containing a list of all disbursements, or empty if none found
     * @throws ServiceException if there is an error retrieving the disbursements
     */
    public Optional<List<Disbursements>> getAllDisbursements() throws ServiceException {
        try {
            logger.debug("Retrieving all disbursements");
            return disbursementsRepository.getAllDisbursements();
        } catch (RepositoryException e) {
            logger.error("Error retrieving all disbursements: {}", e.getMessage());
            throw new ServiceException("Failed to retrieve disbursements", e);
        }
    }

    /**
     * Retrieves a specific disbursement by ID.
     * @param disbursementId The ID of the disbursement to retrieve
     * @return Optional containing the disbursement if found, empty otherwise
     * @throws ServiceException if there is an error retrieving the disbursement
     */
    public Optional<Disbursements> getDisbursementsById(String disbursementId) throws ServiceException {
        try {
            validateDisbursementId(disbursementId);
            logger.debug("Retrieving disbursement with ID: {}", disbursementId);
            return disbursementsRepository.getDisbursementsById(disbursementId);
        } catch (RepositoryException e) {
            logger.error("Error retrieving disbursement with ID {}: {}", disbursementId, e.getMessage());
            throw new ServiceException("Failed to retrieve disbursement", e);
        }
    }

    /**
     * Retrieves all disbursements for a specific client.
     * @param clientId The ID of the client
     * @return Optional containing a list of disbursements for the client, or empty if none found
     * @throws ServiceException if there is an error retrieving the disbursements
     */
    public Optional<List<Disbursements>> getDisbursementsByClientId(String clientId) throws ServiceException {
        try {
            validateClientId(clientId);
            logger.debug("Retrieving disbursements for client with ID: {}", clientId);
            return disbursementsRepository.getDisbursementsByClientId(clientId);
        } catch (RepositoryException e) {
            logger.error("Error retrieving disbursements for client {}: {}", clientId, e.getMessage());
            throw new ServiceException("Failed to retrieve disbursements for client", e);
        }
    }

    /**
     * Retrieves all disbursements for a specific case.
     * @param caseId The ID of the case
     * @return Optional containing a list of disbursements for the case, or empty if none found
     * @throws ServiceException if there is an error retrieving the disbursements
     */
    public Optional<List<Disbursements>> getDisbursementsByCaseId(String caseId) throws ServiceException {
        try {
            validateCaseId(caseId);
            logger.debug("Retrieving disbursements for case with ID: {}", caseId);
            return disbursementsRepository.getDisbursementsByCaseId(caseId);
        } catch (RepositoryException e) {
            logger.error("Error retrieving disbursements for case {}: {}", caseId, e.getMessage());
            throw new ServiceException("Failed to retrieve disbursements for case", e);
        }
    }

    /**
     * Creates a new disbursement.
     * @param disbursement The disbursement to create
     * @throws ServiceException if there is an error creating the disbursement
     */
    public void postDisbursements(Disbursements disbursement) throws ServiceException {
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

    /**
     * Updates an existing disbursement.
     * @param disbursement The disbursement data to update
     * @throws ServiceException if there is an error updating the disbursement
     */
    public void updateDisbursements(Disbursements disbursement) throws ServiceException {
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

    /**
     * Deletes a disbursement.
     * @param disbursementId The ID of the disbursement to delete
     * @return The number of rows affected (1 if successful, 0 if not found)
     * @throws ServiceException if there is an error deleting the disbursement
     */
    public int deleteDisbursementById(String disbursementId) throws ServiceException {
        try {
            validateDisbursementId(disbursementId);
            validateDisbursementExists(disbursementId);
            logger.debug("Deleting disbursement with ID: {}", disbursementId);
            
            Disbursements disbursement = getDisbursementsById(disbursementId)
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
      Optional<Disbursements> disbursement = getDisbursementsById(disbursementId);
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

    private void validateDisbursement(Disbursements disbursement) throws ServiceException {
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
