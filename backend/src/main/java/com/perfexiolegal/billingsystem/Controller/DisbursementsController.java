package com.perfexiolegal.billingsystem.Controller;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.ApiResponse;
import com.perfexiolegal.billingsystem.Model.DisbursementDetails;
import com.perfexiolegal.billingsystem.Service.IDisbursementsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller class for handling disbursement-related HTTP requests.
 * Provides endpoints for managing disbursement data.
 */
@RestController
@RequestMapping("/api/disbursements")
public class DisbursementsController {

    private static final Logger logger = LoggerFactory.getLogger(DisbursementsController.class);

    @Autowired
    private IDisbursementsService disbursementsService;

    /**
     * Retrieves all disbursements.
     * @return ResponseEntity containing a list of all disbursements
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        try {
            logger.debug("Retrieving all disbursements");
            Optional<List<DisbursementDetails>> disbursements = disbursementsService.getAll();
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .data(disbursements.orElse(List.of()))
                    .message("Disbursements retrieved successfully")
                    .build());
        } catch (ServiceException e) {
            logger.error("Error retrieving all disbursements: {}", e.getMessage());
            return ResponseEntity.status(500).body(ApiResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    /**
     * Retrieves a specific disbursement by ID.
     * @param id The ID of the disbursement to retrieve
     * @return ResponseEntity containing the requested disbursement
     */
    @GetMapping("/id={id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable String id) {
        try {
            logger.debug("Retrieving disbursement with ID: {}", id);
            Optional<DisbursementDetails> disbursement = disbursementsService.getById(id);
            if (disbursement.isPresent()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .data(disbursement.get())
                        .message("Disbursement retrieved successfully")
                        .build());
            } else {
                return ResponseEntity.status(404).body(ApiResponse.builder()
                        .success(false)
                        .message("Disbursement not found")
                        .build());
            }
        } catch (ServiceException e) {
            logger.error("Error retrieving disbursement with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(500).body(ApiResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    /**
     * Retrieves all disbursements for a specific case.
     * @param caseId The ID of the case
     * @return ResponseEntity containing a list of disbursements for the case
     */
    @GetMapping("/case={caseId}")
    public ResponseEntity<ApiResponse> getByCaseId(@PathVariable String caseId) {
        try {
            logger.debug("Retrieving disbursements for case with ID: {}", caseId);
            Optional<List<DisbursementDetails>> disbursements = disbursementsService.getByCaseId(caseId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .data(disbursements.orElse(List.of()))
                    .message("Disbursements retrieved successfully")
                    .build());
        } catch (ServiceException e) {
            logger.error("Error retrieving disbursements for case {}: {}", caseId, e.getMessage());
            return ResponseEntity.status(500).body(ApiResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    /**
     * Retrieves all disbursements for a specific client.
     * @param clientId The ID of the client
     * @return ResponseEntity containing a list of disbursements for the client
     */
    @GetMapping("/client={clientId}")
    public ResponseEntity<ApiResponse> getByClientId(@PathVariable String clientId) {
        try {
            logger.debug("Retrieving disbursements for client with ID: {}", clientId);
            Optional<List<DisbursementDetails>> disbursements = disbursementsService.getByClientId(clientId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .data(disbursements.orElse(List.of()))
                    .message("Disbursements retrieved successfully")
                    .build());
        } catch (ServiceException e) {
            logger.error("Error retrieving disbursements for client {}: {}", clientId, e.getMessage());
            return ResponseEntity.status(500).body(ApiResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    /**
     * Creates a new disbursement.
     * @param disbursement The disbursement data to create
     * @return ResponseEntity indicating the result of the creation
     */
    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody DisbursementDetails disbursement) {
        try {
            logger.debug("Creating new disbursement");
            disbursementsService.create(disbursement);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Disbursement created successfully")
                    .build());
        } catch (ServiceException e) {
            logger.error("Error creating disbursement: {}", e.getMessage());
            return ResponseEntity.status(500).body(ApiResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    /**
     * Updates an existing disbursement.
     * @param id The ID of the disbursement to update
     * @param disbursement The updated disbursement data
     * @return ResponseEntity indicating the result of the update
     */
    @PutMapping("/id={id}")
    public ResponseEntity<ApiResponse> update(@PathVariable String id, @RequestBody DisbursementDetails disbursement) {
        try {
            logger.debug("Updating disbursement with ID: {}", id);
            DisbursementDetails updatedDisbursement = DisbursementDetails.builder()
                    .disbursementId(id)
                    .caseId(disbursement.getCaseId())
                    .clientId(disbursement.getClientId())
                    .disbursement(disbursement.getDisbursement())
                    .date(disbursement.getDate())
                    .currencyCode(disbursement.getCurrencyCode())
                    .conversionRate(disbursement.getConversionRate())
                    .inrAmount(disbursement.getInrAmount())
                    .conversionAmount(disbursement.getConversionAmount())
                    .build();
            disbursementsService.update(updatedDisbursement);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Disbursement updated successfully")
                    .build());
        } catch (ServiceException e) {
            logger.error("Error updating disbursement: {}", e.getMessage());
            return ResponseEntity.status(500).body(ApiResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    /**
     * Deletes a disbursement.
     * @param id The ID of the disbursement to delete
     * @return ResponseEntity indicating the result of the deletion
     */
    @DeleteMapping("/id={id}")
    public ResponseEntity<ApiResponse> deleteById(@PathVariable String id) {
        try {
            logger.debug("Deleting disbursement with ID: {}", id);
            disbursementsService.deleteById(id);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Disbursement deleted successfully")
                    .build());
        } catch (ServiceException e) {
            logger.error("Error deleting disbursement: {}", e.getMessage());
            return ResponseEntity.status(500).body(ApiResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }
}
