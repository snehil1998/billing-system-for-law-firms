package com.perfexiolegal.billingsystem.Controller;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.ApiResponse;
import com.perfexiolegal.billingsystem.Model.Cases;
import com.perfexiolegal.billingsystem.Service.CasesService;
import com.perfexiolegal.billingsystem.Transformer.CasesTransformer;
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
@RequestMapping("/backend")
public class CasesController {

    private static final Logger logger = LoggerFactory.getLogger(CasesController.class);

    @Autowired
    private CasesService casesService;

    @Autowired
    private CasesTransformer casesTransformer;

    /**
     * Retrieves all cases.
     * @return ResponseEntity containing a list of all cases
     */
    @GetMapping(value = "/cases", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getAllCases() {
        try {
            logger.debug("Retrieving all cases");
            Optional<List<Cases>> cases = casesService.getAllCases();
            
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
    @GetMapping(value = "/cases={caseID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getCaseById(@PathVariable("caseID") String caseID) {
        try {
            logger.debug("Retrieving case with ID: {}", caseID);
            Optional<Cases> case_ = casesService.getCaseById(caseID);
            
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
    @PostMapping(value = "/cases", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> createCase(@RequestBody Cases case_) {
        try {
            logger.debug("Creating new case with ID: {}", case_.getCaseId());
            Cases createdCase = casesService.postCases(case_);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.builder()
                            .message("Case created successfully")
                            .success(true)
                            .data(createdCase)
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
    @PutMapping(value = "/cases={caseID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updateCase(
            @PathVariable("caseID") String caseID,
            @RequestBody Cases case_) {
        try {
            logger.debug("Updating case with ID: {}", caseID);
            Cases updatedCase = casesService.updateCase(case_);
            
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Case updated successfully")
                    .success(true)
                    .data(updatedCase)
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
    @DeleteMapping(value = "/cases={caseID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteCase(@PathVariable("caseID") String caseID) {
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
    @PutMapping(value = "/cases={caseID}/amounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updateAmounts(
            @PathVariable("caseID") String caseID,
            @RequestParam double disbursementsAmount,
            @RequestParam double servicesAmount) {
        try {
            logger.debug("Updating amounts for case with ID: {}", caseID);
            Cases updatedCase = casesService.updateAmounts(caseID, disbursementsAmount, servicesAmount);
            
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
