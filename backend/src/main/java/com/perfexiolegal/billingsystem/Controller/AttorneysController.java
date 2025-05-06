package com.perfexiolegal.billingsystem.Controller;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.ApiResponse;
import com.perfexiolegal.billingsystem.Model.Attorneys;
import com.perfexiolegal.billingsystem.Service.AttorneysService;
import com.perfexiolegal.billingsystem.Transformer.AttorneysTransformer;
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
 * REST Controller for managing attorney-related operations.
 * Provides endpoints for CRUD operations on attorneys.
 */
@RestController
@RequestMapping("/backend")
public class AttorneysController {

    private static final Logger logger = LoggerFactory.getLogger(AttorneysController.class);
    private final AttorneysService attorneysService;
    private final AttorneysTransformer attorneysTransformer;

    @Autowired
    public AttorneysController(AttorneysService attorneysService, AttorneysTransformer attorneysTransformer) {
        this.attorneysService = attorneysService;
        this.attorneysTransformer = attorneysTransformer;
    }

    /**
     * Retrieves all attorneys from the system.
     * @return ResponseEntity containing the list of attorneys or an empty list
     */
    @GetMapping(value = "/attorneys")
    public ResponseEntity<ApiResponse> getAllAttorneys() {
        try {
            logger.info("Retrieving all attorneys");
            Optional<List<Attorneys>> listOfAttorneys = attorneysService.getAllAttorneys();
            List<Attorneys> attorneys = listOfAttorneys.orElse(List.of());

            return ResponseEntity.ok(ApiResponse.builder()
                    .message(attorneys.isEmpty() ? "No attorneys found" : "Attorneys retrieved successfully")
                    .success(true)
                    .data(attorneys)
                    .build());
        } catch (ServiceException e) {
            logger.error("Failed to retrieve attorneys", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Internal server error")
                            .success(false)
                            .build());
        }
    }

    /**
     * Retrieves a specific attorney by their ID.
     * @param attorneyID The ID of the attorney to retrieve
     * @return ResponseEntity containing the attorney if found
     */
    @GetMapping(value = "/attorneys={attorneyID}")
    public ResponseEntity<ApiResponse> getAttorneysForCase(@PathVariable("attorneyID") String attorneyID) {
        try {
            logger.info("Retrieving attorney with ID: {}", attorneyID);
            Optional<Attorneys> retrievedAttorneys = attorneysService.getAttorneyById(attorneyID);

            if (retrievedAttorneys.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .message("No attorney found with ID: " + attorneyID)
                        .success(true)
                        .build());
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Attorney retrieved successfully")
                    .success(true)
                    .data(retrievedAttorneys.get())
                    .build());
        } catch (ServiceException e) {
            logger.error("Failed to retrieve attorney with ID: {}", attorneyID, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Internal server error")
                            .success(false)
                            .build());
        }
    }

    /**
     * Updates an existing attorney's information.
     * @param attorneyID The ID of the attorney to update
     * @param updatedData The updated attorney data
     * @return ResponseEntity containing the updated attorney
     */
    @PutMapping(value = "/attorneys={attorneyID}")
    public ResponseEntity<ApiResponse> updateAttorney(
            @PathVariable("attorneyID") String attorneyID,
            @RequestBody Attorneys updatedData) {
        try {
            logger.info("Updating attorney with ID: {}", attorneyID);
            Optional<Attorneys> existingAttorney = attorneysService.getAttorneyById(attorneyID);

            if (existingAttorney.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.builder()
                                .message("Cannot find attorney with id=" + attorneyID)
                                .success(false)
                                .build());
            }

            Attorneys updatedAttorney;
            if (updatedData.getServicePricing().get(0).getPrice() < 0) {
                updatedAttorney = attorneysTransformer.deleteServicePrice(
                        existingAttorney.get(),
                        updatedData.getServicePricing().get(0).getClientId());
            } else {
                try {
                    updatedAttorney = attorneysTransformer.update(updatedData, attorneyID);
                } catch (ServiceException e) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(ApiResponse.builder()
                                    .message(e.getMessage())
                                    .success(false)
                                    .build());
                }
            }

            attorneysService.updateAttorney(updatedAttorney);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Attorney was updated successfully")
                    .success(true)
                    .data(updatedAttorney)
                    .build());
        } catch (ServiceException e) {
            logger.error("Failed to update attorney with ID: {}", attorneyID, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Internal server error")
                            .success(false)
                            .build());
        }
    }

    /**
     * Creates a new attorney in the system.
     * @param attorney The attorney data to create
     * @return ResponseEntity containing the created attorney
     */
    @PostMapping(value = "/attorneys", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> createNewAttorney(@RequestBody Attorneys attorney) {
        try {
            logger.info("Creating attorney with ID: {}", attorney.getAttorneyId());
            
            if (attorneysService.getAttorneyById(attorney.getAttorneyId()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.builder()
                                .message("Attorney with ID already exists")
                                .success(false)
                                .build());
            }

            Attorneys createdAttorney = attorneysService.postAttorneys(attorney);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.builder()
                            .message("Attorney was created successfully")
                            .success(true)
                            .data(createdAttorney)
                            .build());
        } catch (ServiceException e) {
            logger.error("Failed to create attorney with ID: {}", attorney.getAttorneyId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Internal server error")
                            .success(false)
                            .build());
        }
    }

    /**
     * Deletes an attorney by their ID.
     * @param attorneyID The ID of the attorney to delete
     * @return ResponseEntity indicating the result of the deletion
     */
    @DeleteMapping(value = "/attorneys={attorneyID}")
    public ResponseEntity<ApiResponse> deleteService(@PathVariable("attorneyID") String attorneyID) {
        try {
            logger.info("Deleting attorney with ID: {}", attorneyID);
            int result = attorneysService.deleteById(attorneyID);

            if (result == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.builder()
                                .message("Cannot find attorney with id=" + attorneyID)
                                .success(false)
                                .build());
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Attorney was deleted successfully")
                    .success(true)
                    .build());
        } catch (ServiceException e) {
            logger.error("Failed to delete attorney with ID: {}", attorneyID, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Cannot delete attorney")
                            .success(false)
                            .build());
        }
    }
}
