package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.DisbursementDetails;
import com.perfexiolegal.billingsystem.Repository.IDisbursementsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DisbursementsService implements IDisbursementsService {
    private static final Logger logger = LoggerFactory.getLogger(DisbursementsService.class);

    @Autowired
    private IDisbursementsRepository disbursementsRepository;

    @Override
    public Optional<List<DisbursementDetails>> getAll() throws ServiceException {
        try {
            logger.debug("Retrieving all disbursements");
            return disbursementsRepository.getAll();
        } catch (Exception e) {
            logger.error("Error retrieving all disbursements: {}", e.getMessage());
            throw new ServiceException("Failed to retrieve disbursements", e);
        }
    }

    @Override
    public Optional<DisbursementDetails> getById(String id) throws ServiceException {
        try {
            logger.debug("Retrieving disbursement with ID: {}", id);
            return disbursementsRepository.getById(id);
        } catch (Exception e) {
            logger.error("Error retrieving disbursement with ID {}: {}", id, e.getMessage());
            throw new ServiceException("Failed to retrieve disbursement", e);
        }
    }

    @Override
    public void create(DisbursementDetails entity) throws ServiceException {
        try {
            validateDisbursement(entity);
            logger.debug("Creating disbursement with ID: {}", entity.getDisbursementId());
            disbursementsRepository.create(entity);
        } catch (Exception e) {
            logger.error("Error creating disbursement: {}", e.getMessage());
            throw new ServiceException("Failed to create disbursement", e);
        }
    }

    @Override
    public void update(DisbursementDetails entity) throws ServiceException {
        try {
            validateDisbursement(entity);
            logger.debug("Updating disbursement with ID: {}", entity.getDisbursementId());
            disbursementsRepository.update(entity);
        } catch (Exception e) {
            logger.error("Error updating disbursement: {}", e.getMessage());
            throw new ServiceException("Failed to update disbursement", e);
        }
    }

    @Override
    public int deleteById(String id) throws ServiceException {
        try {
            logger.debug("Deleting disbursement with ID: {}", id);
            return disbursementsRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting disbursement: {}", e.getMessage());
            throw new ServiceException("Failed to delete disbursement", e);
        }
    }

    @Override
    public Optional<List<DisbursementDetails>> getByClientId(String clientID) throws ServiceException {
        try {
            logger.debug("Retrieving disbursements for client with ID: {}", clientID);
            return disbursementsRepository.getByClientId(clientID);
        } catch (Exception e) {
            logger.error("Error retrieving disbursements for client {}: {}", clientID, e.getMessage());
            throw new ServiceException("Failed to retrieve disbursements for client", e);
        }
    }

    @Override
    public Optional<List<DisbursementDetails>> getByCaseId(String caseID) throws ServiceException {
        try {
            logger.debug("Retrieving disbursements for case with ID: {}", caseID);
            return disbursementsRepository.getByCaseId(caseID);
        } catch (Exception e) {
            logger.error("Error retrieving disbursements for case {}: {}", caseID, e.getMessage());
            throw new ServiceException("Failed to retrieve disbursements for case", e);
        }
    }

    private void validateDisbursement(DisbursementDetails disbursement) throws ServiceException {
        if (disbursement == null) {
            throw new ServiceException("Disbursement details cannot be null");
        }
        if (disbursement.getDisbursementId() == null || disbursement.getDisbursementId().trim().isEmpty()) {
            throw new ServiceException("Disbursement ID cannot be null or empty");
        }
        if (disbursement.getCaseId() == null || disbursement.getCaseId().trim().isEmpty()) {
            throw new ServiceException("Case ID cannot be null or empty");
        }
        if (disbursement.getClientId() == null || disbursement.getClientId().trim().isEmpty()) {
            throw new ServiceException("Client ID cannot be null or empty");
        }
        if (disbursement.getDisbursement() == null || disbursement.getDisbursement().trim().isEmpty()) {
            throw new ServiceException("Disbursement description cannot be null or empty");
        }
        if (disbursement.getDate() == null) {
            throw new ServiceException("Date cannot be null");
        }
        if (disbursement.getCurrencyCode() == null || disbursement.getCurrencyCode().trim().isEmpty()) {
            throw new ServiceException("Currency code cannot be null or empty");
        }
        if (disbursement.getConversionRate() <= 0) {
            throw new ServiceException("Conversion rate must be greater than 0");
        }
        if (disbursement.getInrAmount() < 0) {
            throw new ServiceException("INR amount cannot be negative");
        }
        if (disbursement.getConversionAmount() < 0) {
            throw new ServiceException("Conversion amount cannot be negative");
        }
    }
}
