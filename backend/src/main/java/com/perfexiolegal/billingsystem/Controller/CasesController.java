package com.perfexiolegal.billingsystem.Controller;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.ApiResponse;
import com.perfexiolegal.billingsystem.Model.CaseDetails;
import com.perfexiolegal.billingsystem.Service.ICasesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller class for handling case-related HTTP requests.
 * Provides endpoints for managing case data.
 */
@RestController
@RequestMapping("/api/cases")
public class CasesController {

    private static final Logger logger = LoggerFactory.getLogger(CasesController.class);

    @Autowired
    private ICasesService casesService;

    /**
     * Retrieves all cases.
     * @return ResponseEntity containing a list of all cases
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        try {
            logger.debug("Retrieving all cases");
            Optional<List<CaseDetails>> cases = casesService.getAll();
            
            if (cases.isEmpty() || cases.get().isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .message("No cases found")
                        .success(true)
                        .data(cases.orElse(List.of()))
                        .build());
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Cases retrieved successfully")
                    .success(true)
                    .data(cases.get())
                    .build());
        } catch (ServiceException e) {
            logger.error("Error retrieving cases: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to retrieve cases: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Retrieves a specific case by ID.
     * @param caseID The ID of the case to retrieve
     * @return ResponseEntity containing the requested case
     */
    @GetMapping(value = "/id={caseID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getById(@PathVariable("caseID") String caseID) {
        try {
            logger.debug("Retrieving case with ID: {}", caseID);
            Optional<CaseDetails> case_ = casesService.getById(caseID);
            
            if (case_.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.builder()
                                .message("Case not found with ID: " + caseID)
                                .success(false)
                                .build());
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Case retrieved successfully")
                    .success(true)
                    .data(case_.get())
                    .build());
        } catch (ServiceException e) {
            logger.error("Error retrieving case with ID {}: {}", caseID, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to retrieve case: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Creates a new case.
     * @param case_ The case data to create
     * @return ResponseEntity containing the created case
     */
    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody CaseDetails caseDetails) {
        try {
            logger.debug("Creating new case with ID: {}", caseDetails.getCaseId());
            casesService.create(caseDetails);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.builder()
                            .message("Case created successfully")
                            .success(true)
                            .data(caseDetails)
                            .build());
        } catch (ServiceException e) {
            logger.error("Error creating case: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to create case: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Updates an existing case.
     * @param caseID The ID of the case to update
     * @param case_ The updated case data
     * @return ResponseEntity containing the updated case
     */
    @PutMapping(value = "/id={caseID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> update(
            @PathVariable("caseID") String caseID,
            @RequestBody CaseDetails caseDetails) {
        try {
            logger.debug("Updating case with ID: {}", caseID);
            casesService.update(caseDetails);
            
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Case updated successfully")
                    .success(true)
                    .data(caseDetails)
                    .build());
        } catch (ServiceException e) {
            logger.error("Error updating case with ID {}: {}", caseID, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to update case: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Deletes a case.
     * @param caseID The ID of the case to delete
     * @return ResponseEntity indicating the result of the deletion
     */
    @DeleteMapping(value = "/id={caseID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteById(@PathVariable("caseID") String caseID) {
        try {
            logger.debug("Deleting case with ID: {}", caseID);
            int result = casesService.deleteById(caseID);
            
            if (result == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.builder()
                                .message("Case not found with ID: " + caseID)
                                .success(false)
                                .build());
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Case deleted successfully")
                    .success(true)
                    .build());
        } catch (ServiceException e) {
            logger.error("Error deleting case with ID {}: {}", caseID, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to delete case: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Updates the amounts for a case.
     * @param caseID The ID of the case to update
     * @param disbursementsAmount The amount to add to disbursements
     * @param servicesAmount The amount to add to services
     * @return ResponseEntity containing the updated case
     */
    @PutMapping(value = "/amounts/id={caseID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updateAmounts(
            @PathVariable("caseID") String caseID,
            @RequestParam double disbursementsAmount,
            @RequestParam double servicesAmount) {
        try {
            logger.debug("Updating amounts for case with ID: {}", caseID);
            CaseDetails updatedCase = casesService.updateAmounts(caseID, disbursementsAmount, servicesAmount);
            
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Case amounts updated successfully")
                    .success(true)
                    .data(updatedCase)
                    .build());
        } catch (ServiceException e) {
            logger.error("Error updating amounts for case with ID {}: {}", caseID, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to update case amounts: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }
}
