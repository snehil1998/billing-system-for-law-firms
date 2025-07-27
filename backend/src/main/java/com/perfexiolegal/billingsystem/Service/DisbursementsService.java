package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.DisbursementRequest;
import com.perfexiolegal.billingsystem.Model.DisbursementResponse;
import com.perfexiolegal.billingsystem.Model.DisbursementRequestDto;
import com.perfexiolegal.billingsystem.Repository.IDisbursementsRepository;
import com.perfexiolegal.billingsystem.Transformer.DisbursementsTransformer;

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

    @Autowired
    private DisbursementsTransformer disbursementsTransformer;

    @Override
    public Optional<List<DisbursementResponse>> getAll() throws ServiceException {
        try {
            logger.debug("Retrieving all disbursements");
            return disbursementsRepository.getAll();
        } catch (Exception e) {
            logger.error("Error retrieving all disbursements: {}", e.getMessage());
            throw new ServiceException("Failed to retrieve disbursements", e);
        }
    }

    @Override
    public Optional<DisbursementResponse> getById(Long disbursementId) throws ServiceException {
        try {
            logger.debug("Retrieving disbursement with ID: {}", disbursementId);
            return disbursementsRepository.getById(disbursementId);
        } catch (Exception e) {
            logger.error("Error retrieving disbursement with ID {}: {}", disbursementId, e.getMessage());
            throw new ServiceException("Failed to retrieve disbursement", e);
        }
    }

    @Override
    public void create(DisbursementRequest details) throws ServiceException {
        try {
            logger.debug("Creating disbursement for case: {}", details.getCaseId());
            casesService.updateAmounts(details.getCaseId(), details.getConversionAmount(), 0);
            clientsService.updateAmounts(details.getClientId(), details.getConversionAmount(), 0);
            disbursementsRepository.create(details);
        } catch (Exception e) {
            logger.error("Error creating disbursement: {}", e.getMessage());
            throw new ServiceException("Failed to create disbursement", e);
        }
    }

    @Override
    public void update(Long disbursementId, DisbursementRequest details) throws ServiceException {
        try {
            logger.debug("Updating disbursement for caseID: {}", details.getCaseId());
            DisbursementResponse disbursement = getDisbursementIfExists(disbursementId);
            DisbursementRequestDto disbursementRequestDto = disbursementsTransformer.ToDisbursementRequestDto(details);
            disbursementsRepository.update(disbursementId, disbursementRequestDto);
            double updatedAmount = disbursementRequestDto.getConversionAmount() - disbursement.getConversionAmount();
            casesService.updateAmounts(details.getCaseId(), 0, updatedAmount);
            clientsService.updateAmounts(details.getClientId(), 0, updatedAmount);
        } catch (Exception e) {
            logger.error("Error updating disbursement: {}", e.getMessage());
            throw new ServiceException("Failed to update disbursement", e);
        }
    }

    @Override
    public int deleteById(Long disbursementId) throws ServiceException {
        try {
            logger.debug("Deleting disbursement with ID: {}", disbursementId);
            DisbursementResponse details = getDisbursementIfExists(disbursementId);
            int result = disbursementsRepository.deleteById(disbursementId);
            casesService.updateAmounts(details.getCaseId(), -details.getConversionAmount(), 0);
            clientsService.updateAmounts(details.getClientId(), -details.getConversionAmount(), 0);
            return result;
        } catch (Exception e) {
            logger.error("Error deleting disbursement: {}", e.getMessage());
            throw new ServiceException("Failed to delete disbursement", e);
        }
    }

    @Override
    public Optional<List<DisbursementResponse>> getByClientId(String clientID) throws ServiceException {
        try {
            logger.debug("Retrieving disbursements for client with ID: {}", clientID);
            return disbursementsRepository.getByClientId(clientID);
        } catch (Exception e) {
            logger.error("Error retrieving disbursements for client {}: {}", clientID, e.getMessage());
            throw new ServiceException("Failed to retrieve disbursements for client", e);
        }
    }

    @Override
    public Optional<List<DisbursementResponse>> getByCaseId(String caseID) throws ServiceException {
        try {
            logger.debug("Retrieving disbursements for case with ID: {}", caseID);
            return disbursementsRepository.getByCaseId(caseID);
        } catch (Exception e) {
            logger.error("Error retrieving disbursements for case {}: {}", caseID, e.getMessage());
            throw new ServiceException("Failed to retrieve disbursements for case", e);
        }
    }

    private DisbursementResponse getDisbursementIfExists(Long disbursementId) throws ServiceException {
        Optional<DisbursementResponse> disbursement = getById(disbursementId);
        if (disbursement.isEmpty()) {
            throw new ServiceException("Disbursement not found with ID: " + disbursementId);
        }
        return disbursement.get();
      }
}
