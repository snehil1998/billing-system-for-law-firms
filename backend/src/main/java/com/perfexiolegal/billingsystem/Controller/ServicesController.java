package com.perfexiolegal.billingsystem.Controller;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.ApiResponse;
import com.perfexiolegal.billingsystem.Model.Services;
import com.perfexiolegal.billingsystem.Model.ServiceDetails;
import com.perfexiolegal.billingsystem.Service.ServicesService;
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
 * Controller class for handling service-related HTTP requests.
 * Provides endpoints for managing service data.
 */
@RestController
@RequestMapping("/backend")
public class ServicesController {

    private static final Logger logger = LoggerFactory.getLogger(ServicesController.class);

    @Autowired
    private ServicesService servicesService;

    /**
     * Retrieves all services.
     * @return ResponseEntity containing a list of all services
     */
    @GetMapping(value = "/services", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getAllServices() {
        try {
            logger.debug("Retrieving all services");
            Optional<List<Services>> listOfServices = servicesService.getAllServices();
            List<Services> services = listOfServices.get();
            
            if (services.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .message("No services found")
                        .success(true)
                        .data(services)
                        .build());
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Services retrieved successfully")
                    .success(true)
                    .data(services)
                    .build());
        } catch (ServiceException e) {
            logger.error("Error retrieving services: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to retrieve services: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Retrieves all services for a specific case.
     * @param caseID The ID of the case
     * @return ResponseEntity containing a list of services for the case
     */
    @GetMapping(value = "/services/case={caseID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getServicesForCase(@PathVariable("caseID") String caseID) {
        try {
            logger.debug("Retrieving services for case with ID: {}", caseID);
            Optional<List<Services>> listOfServices = servicesService.getServicesForCase(caseID);
            List<Services> services = listOfServices.get();
            
            if (services.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .message("No services found for case: " + caseID)
                        .success(true)
                        .data(services)
                        .build());
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Services retrieved successfully")
                    .success(true)
                    .data(services)
                    .build());
        } catch (ServiceException e) {
            logger.error("Error retrieving services for case {}: {}", caseID, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to retrieve services: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Retrieves a specific service by ID.
     * @param serviceID The ID of the service to retrieve
     * @return ResponseEntity containing the requested service
     */
    @GetMapping(value = "/services={serviceID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getServicesFromId(@PathVariable("serviceID") String serviceID) {
        try {
            logger.debug("Retrieving service with ID: {}", serviceID);
            Optional<Services> retrievedService = servicesService.getServiceFromId(serviceID);
            
            if (retrievedService.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.builder()
                                .message("No service found with ID: " + serviceID)
                                .success(false)
                                .build());
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Service retrieved successfully")
                    .success(true)
                    .data(retrievedService.get())
                    .build());
        } catch (ServiceException e) {
            logger.error("Error retrieving service with ID {}: {}", serviceID, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to retrieve service: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Retrieves all services for a specific client.
     * @param clientID The ID of the client
     * @return ResponseEntity containing a list of services for the client
     */
    @GetMapping(value = "/services/client={clientID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getServicesForClient(@PathVariable("clientID") String clientID) {
        try {
            logger.debug("Retrieving services for client with ID: {}", clientID);
            Optional<List<Services>> listOfServices = servicesService.getServicesForClient(clientID);
            List<Services> services = listOfServices.get();
            
            if (services.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .message("No services found for client: " + clientID)
                        .success(true)
                        .data(services)
                        .build());
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Services retrieved successfully")
                    .success(true)
                    .data(services)
                    .build());
        } catch (ServiceException e) {
            logger.error("Error retrieving services for client {}: {}", clientID, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to retrieve services: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Creates a new service.
     * @param serviceDetails The service data to create
     * @return ResponseEntity containing the created service
     */
    @PostMapping(value = "/services", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> createNewService(@RequestBody ServiceDetails serviceDetails) {
        try {
            logger.debug("Creating service with name: {}", serviceDetails.getService());
            servicesService.postServices(serviceDetails);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.builder()
                            .message("Service created successfully")
                            .success(true)
                            .data(serviceDetails)
                            .build());
        } catch (ServiceException e) {
            logger.error("Error creating service: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to create service: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Updates an existing service.
     * @param serviceID The ID of the service to update
     * @param serviceDetails The updated service data
     * @return ResponseEntity containing the updated service
     */
    @PutMapping(value = "/services={serviceID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updateService(
            @PathVariable("serviceID") String serviceID,
            @RequestBody ServiceDetails serviceDetails) {
        try {
            logger.debug("Updating service with ID: {}", serviceID);
            servicesService.updateServices(serviceDetails);
            
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Service updated successfully")
                    .success(true)
                    .data(serviceDetails)
                    .build());
        } catch (ServiceException e) {
            logger.error("Error updating service with ID {}: {}", serviceID, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to update service: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Deletes a service.
     * @param serviceID The ID of the service to delete
     * @return ResponseEntity indicating the result of the deletion
     */
    @DeleteMapping(value = "/services={serviceID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteService(@PathVariable("serviceID") String serviceID) {
        try {
            logger.debug("Deleting service with ID: {}", serviceID);
            int result = servicesService.deleteById(serviceID);
            
            if (result == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.builder()
                                .message("Service not found with ID: " + serviceID)
                                .success(false)
                                .build());
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Service deleted successfully")
                    .success(true)
                    .build());
        } catch (ServiceException e) {
            logger.error("Error deleting service with ID {}: {}", serviceID, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to delete service: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Deletes all services for a specific case.
     * @param caseID The ID of the case
     * @return ResponseEntity indicating the result of the deletion
     */
    @DeleteMapping(value = "/services/case={caseID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteByCase(@PathVariable("caseID") String caseID) {
        try {
            logger.debug("Deleting services for case with ID: {}", caseID);
            int result = servicesService.deleteByCase(caseID);
            
            if (result == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.builder()
                                .message("No services found for case: " + caseID)
                                .success(false)
                                .build());
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Services deleted successfully")
                    .success(true)
                    .build());
        } catch (ServiceException e) {
            logger.error("Error deleting services for case {}: {}", caseID, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to delete services: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }
}
