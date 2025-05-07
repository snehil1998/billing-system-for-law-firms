package com.perfexiolegal.billingsystem.Controller;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.ApiResponse;
import com.perfexiolegal.billingsystem.Model.Clients;
import com.perfexiolegal.billingsystem.Model.ClientsWithoutId;
import com.perfexiolegal.billingsystem.Service.ClientsService;
import com.perfexiolegal.billingsystem.Transformer.ClientsTransformer;
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
 * Controller class for handling client-related HTTP requests.
 * Provides endpoints for managing client data.
 */
@RestController
@RequestMapping("/backend")
public class ClientsController {

    private static final Logger logger = LoggerFactory.getLogger(ClientsController.class);

    @Autowired
    private ClientsService clientsService;

    @Autowired
    private ClientsTransformer clientsTransformer;

    /**
     * Retrieves all clients.
     * @return ResponseEntity containing a list of all clients
     */
    @GetMapping(value = "/clients", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getAllClients() {
        try {
            logger.debug("Retrieving all clients");
            Optional<List<Clients>> clients = clientsService.getAllClients();
            
            if (clients.isEmpty() || clients.get().isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .message("No clients found")
                        .success(true)
                        .data(clients.orElse(List.of()))
                        .build());
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Clients retrieved successfully")
                    .success(true)
                    .data(clients.get())
                    .build());
        } catch (ServiceException e) {
            logger.error("Error retrieving clients: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to retrieve clients: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Retrieves a specific client by ID.
     * @param clientID The ID of the client to retrieve
     * @return ResponseEntity containing the requested client
     */
    @GetMapping(value = "/clients/{clientID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getClientById(@PathVariable("clientID") String clientID) {
        try {
            logger.debug("Retrieving client with ID: {}", clientID);
            Optional<Clients> client = clientsService.getClientById(clientID);
            
            if (client.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.builder()
                                .message("Client not found with ID: " + clientID)
                                .success(false)
                                .build());
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Client retrieved successfully")
                    .success(true)
                    .data(client.get())
                    .build());
        } catch (ServiceException e) {
            logger.error("Error retrieving client with ID {}: {}", clientID, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to retrieve client: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Creates a new client.
     * @param client The client data to create
     * @return ResponseEntity containing the created client
     */
    @PostMapping(value = "/clients", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> createClient(@RequestBody Clients client) {
        try {
            logger.debug("Creating new client with ID: {}", client.getClientId());
            Clients createdClient = clientsService.postClients(client);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.builder()
                            .message("Client created successfully")
                            .success(true)
                            .data(createdClient)
                            .build());
        } catch (ServiceException e) {
            logger.error("Error creating client: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to create client: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Updates an existing client.
     * @param clientID The ID of the client to update
     * @param client The updated client data
     * @return ResponseEntity containing the updated client
     */
    @PutMapping(value = "/clients/{clientID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updateClient(
            @PathVariable("clientID") String clientID,
            @RequestBody Clients client) {
        try {
            logger.debug("Updating client with ID: {}", clientID);
            Clients updatedClient = clientsService.updateClient(client);
            
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Client updated successfully")
                    .success(true)
                    .data(updatedClient)
                    .build());
        } catch (ServiceException e) {
            logger.error("Error updating client with ID {}: {}", clientID, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to update client: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Deletes a client.
     * @param clientID The ID of the client to delete
     * @return ResponseEntity indicating the result of the deletion
     */
    @DeleteMapping(value = "/clients={clientID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteClient(@PathVariable("clientID") String clientID) {
        try {
            logger.debug("Deleting client with ID: {}", clientID);
            int result = clientsService.deleteById(clientID);
            
            if (result == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.builder()
                                .message("Client not found with ID: " + clientID)
                                .success(false)
                                .build());
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Client deleted successfully")
                    .success(true)
                    .build());
        } catch (ServiceException e) {
            logger.error("Error deleting client with ID {}: {}", clientID, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to delete client: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }

    /**
     * Updates the amounts for a client.
     * @param clientID The ID of the client to update
     * @param disbursementsAmount The amount to add to disbursements
     * @param servicesAmount The amount to add to services
     * @return ResponseEntity containing the updated client
     */
    @PutMapping(value = "/clients/{clientID}/amounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updateAmounts(
            @PathVariable("clientID") String clientID,
            @RequestParam double disbursementsAmount,
            @RequestParam double servicesAmount) {
        try {
            logger.debug("Updating amounts for client with ID: {}", clientID);
            Clients updatedClient = clientsService.updateAmounts(clientID, disbursementsAmount, servicesAmount);
            
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Client amounts updated successfully")
                    .success(true)
                    .data(updatedClient)
                    .build());
        } catch (ServiceException e) {
            logger.error("Error updating amounts for client with ID {}: {}", clientID, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Failed to update client amounts: " + e.getMessage())
                            .success(false)
                            .build());
        }
    }
}
