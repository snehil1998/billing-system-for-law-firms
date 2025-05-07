package com.perfexiolegal.billingsystem.Controller;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.ApiResponse;
import com.perfexiolegal.billingsystem.Model.Disbursements;
import com.perfexiolegal.billingsystem.Service.DisbursementsService;
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
 * Controller class for handling disbursement-related HTTP requests.
 * Provides endpoints for managing disbursement data.
 */
@RestController
@RequestMapping("/backend")
public class DisbursementsController {

    private static final Logger logger = LoggerFactory.getLogger(DisbursementsController.class);

    @Autowired
    private DisbursementsService disbursementsService;

    /**
     * Retrieves all disbursements.
     * @return ResponseEntity containing a list of all disbursements
     */
    @GetMapping(value = "/disbursements", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getAllDisbursements() {
        try {
            logger.debug("Retrieving all disbursements");
            Optional<List<Disbursements>> listOfDisbursements = disbursementsService.getAllDisbursements();
            List<Disbursements> disbursements = listOfDisbursements.get();
            
            if (disbursements.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .message("No disbursements found")
                        .success(true)
                        .data(disbursements)
                        .build());
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Disbursements retrieved successfully")
                    .success(true)
                    .data(disbursements)
                    .build());
        } catch (ServiceException e) {
            logger.error("Error retrieving disbursements: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to retrieve disbursements: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Retrieves a specific disbursement by ID.
     * @param disbursementID The ID of the disbursement to retrieve
     * @return ResponseEntity containing the requested disbursement
     */
    @GetMapping(value = "/disbursements={disbursementID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getDisbursementForId(@PathVariable("disbursementID") String disbursementID) {
        try {
            logger.debug("Retrieving disbursement with ID: {}", disbursementID);
            Optional<Disbursements> retrievedDisbursement = disbursementsService.getDisbursementsById(disbursementID);
            
            if (retrievedDisbursement.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.builder()
                                .message("No disbursement found with ID: " + disbursementID)
                                .success(false)
                                .build());
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Disbursement retrieved successfully")
                    .success(true)
                    .data(retrievedDisbursement.get())
                    .build());
        } catch (ServiceException e) {
            logger.error("Error retrieving disbursement with ID {}: {}", disbursementID, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to retrieve disbursement: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Retrieves all disbursements for a specific case.
     * @param caseID The ID of the case
     * @return ResponseEntity containing a list of disbursements for the case
     */
    @GetMapping(value = "/disbursements/case={caseID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getDisbursementsForCase(@PathVariable("caseID") String caseID) {
        try {
            logger.debug("Retrieving disbursements for case with ID: {}", caseID);
            Optional<List<Disbursements>> listOfDisbursements = disbursementsService.getDisbursementsByCaseId(caseID);
            List<Disbursements> disbursements = listOfDisbursements.get();
            
            if (disbursements.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .message("No disbursements found for case: " + caseID)
                        .success(true)
                        .data(disbursements)
                        .build());
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Disbursements retrieved successfully")
                    .success(true)
                    .data(disbursements)
                    .build());
        } catch (ServiceException e) {
            logger.error("Error retrieving disbursements for case {}: {}", caseID, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to retrieve disbursements: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Retrieves all disbursements for a specific client.
     * @param clientID The ID of the client
     * @return ResponseEntity containing a list of disbursements for the client
     */
    @GetMapping(value = "/disbursements/client={clientID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getDisbursementsForClient(@PathVariable("clientID") String clientID) {
        try {
            logger.debug("Retrieving disbursements for client with ID: {}", clientID);
            Optional<List<Disbursements>> listOfDisbursements = disbursementsService.getDisbursementsByClientId(clientID);
            List<Disbursements> disbursements = listOfDisbursements.get();
            
            if (disbursements.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .message("No disbursements found for client: " + clientID)
                        .success(true)
                        .data(disbursements)
                        .build());
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Disbursements retrieved successfully")
                    .success(true)
                    .data(disbursements)
                    .build());
        } catch (ServiceException e) {
            logger.error("Error retrieving disbursements for client {}: {}", clientID, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to retrieve disbursements: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Creates a new disbursement.
     * @param disbursementJSON The disbursement data to create
     * @return ResponseEntity containing the created disbursement
     */
    @PostMapping(value = "/disbursements", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> createNewDisbursement(@RequestBody Disbursements disbursement) {
        try {
            logger.debug("Creating disbursement with name: {}", disbursement.getDisbursement());
            disbursementsService.postDisbursements(disbursement);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.builder()
                            .message("Disbursement created successfully")
                            .success(true)
                            .data(disbursement)
                            .build());
        } catch (ServiceException e) {
            logger.error("Error creating disbursement: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to create disbursement: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Updates an existing disbursement.
     * @param disbursementID The ID of the disbursement to update
     * @param disbursement The updated disbursement data
     * @return ResponseEntity containing the updated disbursement
     */
    @PutMapping(value = "/disbursements={disbursementID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updateDisbursement(
            @PathVariable("disbursementID") String disbursementID,
            @RequestBody Disbursements disbursement) {
        try {
            logger.debug("Updating disbursement with ID: {}", disbursementID);
            disbursementsService.updateDisbursements(disbursement);
            
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Disbursement updated successfully")
                    .success(true)
                    .data(disbursement)
                    .build());
        } catch (ServiceException e) {
            logger.error("Error updating disbursement with ID {}: {}", disbursementID, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to update disbursement: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Deletes a disbursement.
     * @param disbursementID The ID of the disbursement to delete
     * @return ResponseEntity indicating the result of the deletion
     */
    @DeleteMapping(value = "/disbursements={disbursementID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteDisbursement(@PathVariable("disbursementID") String disbursementID) {
        try {
            logger.debug("Deleting disbursement with ID: {}", disbursementID);
            int result = disbursementsService.deleteDisbursementById(disbursementID);
            
            if (result == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.builder()
                                .message("Disbursement not found with ID: " + disbursementID)
                                .success(false)
                                .build());
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Disbursement deleted successfully")
                    .success(true)
                    .build());
        } catch (ServiceException e) {
            logger.error("Error deleting disbursement with ID {}: {}", disbursementID, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to delete disbursement: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }
}
