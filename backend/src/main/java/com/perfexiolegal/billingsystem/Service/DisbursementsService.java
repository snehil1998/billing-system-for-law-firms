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

    @Autowired
    private ICasesService casesService;

    @Autowired
    private IClientsService clientsService;

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
    public Optional<DisbursementDetails> getById(String disbursementId) throws ServiceException {
        try {
            logger.debug("Retrieving disbursement with ID: {}", disbursementId);
            return disbursementsRepository.getById(disbursementId);
        } catch (Exception e) {
            logger.error("Error retrieving disbursement with ID {}: {}", disbursementId, e.getMessage());
            throw new ServiceException("Failed to retrieve disbursement", e);
        }
    }

    @Override
    public void create(DisbursementDetails details) throws ServiceException {
        try {
            logger.debug("Creating disbursement with ID: {}", details.getDisbursementId());
            casesService.updateAmounts(details.getCaseId(), details.getConversionAmount(), 0);
            clientsService.updateAmounts(details.getClientId(), details.getConversionAmount(), 0);
            disbursementsRepository.create(details);
        } catch (Exception e) {
            logger.error("Error creating disbursement: {}", e.getMessage());
            throw new ServiceException("Failed to create disbursement", e);
        }
    }

    @Override
    public void update(DisbursementDetails details) throws ServiceException {
        try {
            logger.debug("Updating disbursement with ID: {}", details.getDisbursementId());
            getDisbursementIfExists(details.getDisbursementId());
            disbursementsRepository.update(details);
        } catch (Exception e) {
            logger.error("Error updating disbursement: {}", e.getMessage());
            throw new ServiceException("Failed to update disbursement", e);
        }
    }

    @Override
    public int deleteById(String disbursementId) throws ServiceException {
        try {
            logger.debug("Deleting disbursement with ID: {}", disbursementId);
            DisbursementDetails details = getDisbursementIfExists(disbursementId);
            casesService.updateAmounts(details.getCaseId(), -details.getConversionAmount(), 0);
            clientsService.updateAmounts(details.getClientId(), -details.getConversionAmount(), 0);
            return disbursementsRepository.deleteById(disbursementId);
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

    private DisbursementDetails getDisbursementIfExists(String disbursementId) throws ServiceException {
        Optional<DisbursementDetails> disbursement = getById(disbursementId);
        if (disbursement.isEmpty()) {
            throw new ServiceException("Disbursement not found with ID: " + disbursementId);
        }
        return disbursement.get();
      }
}
