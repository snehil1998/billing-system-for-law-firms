package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.Clients;
import com.perfexiolegal.billingsystem.Repository.ClientsRepository;
import com.perfexiolegal.billingsystem.Transformer.ClientsTransformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

/**
 * Service class for handling client-related business logic.
 * Acts as an intermediary between the controller and repository layers.
 */
@Service
public class ClientsService {

    private static final Logger logger = LoggerFactory.getLogger(ClientsService.class);

    @Autowired
    private ClientsRepository clientsRepository;
    @Autowired
    private ClientsTransformer clientsTransformer;

    /**
     * Retrieves all clients from the system.
     * @return Optional containing a list of all clients, or empty if none found
     * @throws ServiceException if there is an error retrieving the clients
     */
    public Optional<List<Clients>> getAllClients() throws ServiceException {
        try {
            logger.debug("Retrieving all clients");
            return clientsRepository.getAllClients();
        } catch (RepositoryException e) {
            logger.error("Error retrieving all clients: {}", e.getMessage());
            throw new ServiceException("Failed to retrieve clients", e);
        }
    }

    /**
     * Retrieves a specific client by their ID.
     * @param clientID The ID of the client to retrieve
     * @return Optional containing the client if found, empty otherwise
     * @throws ServiceException if there is an error retrieving the client
     */
    public Optional<Clients> getClientById(String clientID) throws ServiceException {
        if (!StringUtils.hasText(clientID)) {
            throw new ServiceException("Client ID cannot be empty");
        }

        try {
            logger.debug("Retrieving client with ID: {}", clientID);
            return clientsRepository.getClientsById(clientID);
        } catch (RepositoryException e) {
            logger.error("Error retrieving client with ID {}: {}", clientID, e.getMessage());
            throw new ServiceException("Failed to retrieve client", e);
        }
    }

    /**
     * Creates a new client in the system.
     * @param client The client to create
     * @return The created client
     * @throws ServiceException if there is an error creating the client or if validation fails
     */
    public Clients postClients(Clients client) throws ServiceException {
        validateClient(client);
        validateClientDoesNotExist(client.getClientId());

        try {
            logger.debug("Creating new client with ID: {}", client.getClientId());
            return clientsRepository.postClients(client);
        } catch (RepositoryException e) {
            logger.error("Error creating client: {}", e.getMessage());
            throw new ServiceException("Failed to create client", e);
        }
    }

    /**
     * Updates an existing client in the system.
     * @param client The client data to update
     * @return The updated client
     * @throws ServiceException if there is an error updating the client or if validation fails
     */
    public Clients updateClient(Clients client) throws ServiceException {
        validateClient(client);
        validateClientExists(client.getClientId());

        try {
            logger.debug("Updating client with ID: {}", client.getClientId());
            return clientsRepository.updateClients(client);
        } catch (RepositoryException e) {
            logger.error("Error updating client: {}", e.getMessage());
            throw new ServiceException("Failed to update client", e);
        }
    }

    /**
     * Deletes a client from the system.
     * @param clientID The ID of the client to delete
     * @return The number of rows affected (1 if successful, 0 if not found)
     * @throws ServiceException if there is an error deleting the client
     */
    public int deleteById(String clientID) throws ServiceException {
        if (!StringUtils.hasText(clientID)) {
            throw new ServiceException("Client ID cannot be empty");
        }

        validateClientExists(clientID);

        try {
            logger.debug("Deleting client with ID: {}", clientID);
            return clientsRepository.deleteById(clientID);
        } catch (RepositoryException e) {
            logger.error("Error deleting client: {}", e.getMessage());
            throw new ServiceException("Failed to delete client", e);
        }
    }

    /**
     * Updates the amounts for a client.
     * @param clientID The ID of the client to update
     * @param disbursementsAmount The amount to add to disbursements
     * @param servicesAmount The amount to add to services
     * @return The updated client
     * @throws ServiceException if there is an error updating the amounts
     */
    public Clients updateAmounts(String clientID, double disbursementsAmount, double servicesAmount) 
            throws ServiceException {
        if (!StringUtils.hasText(clientID)) {
            throw new ServiceException("Client ID cannot be empty");
        }

        if (disbursementsAmount < 0 || servicesAmount < 0) {
            throw new ServiceException("Amounts cannot be negative");
        }

        Optional<Clients> existingClient = getClientById(clientID);
        if (existingClient.isEmpty()) {
            throw new ServiceException("Client not found with ID: " + clientID);
        }

        Clients updatedClient = clientsTransformer.updateAmount(existingClient.get(), disbursementsAmount, servicesAmount);
        return updateClient(updatedClient);
    }

    /**
     * Validates a client object.
     * @param client The client to validate
     * @throws ServiceException if validation fails
     */
    private void validateClient(Clients client) throws ServiceException {
        if (client == null) {
            throw new ServiceException("Client cannot be null");
        }

        if (!StringUtils.hasText(client.getClientId())) {
            throw new ServiceException("Client ID cannot be empty");
        }

        if (!StringUtils.hasText(client.getClientName())) {
            throw new ServiceException("Client name cannot be empty");
        }

        if (!StringUtils.hasText(client.getCurrencyCode())) {
            throw new ServiceException("Currency code cannot be empty");
        }

        try {
            Currency.getInstance(client.getCurrencyCode());
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Invalid currency code: " + client.getCurrencyCode());
        }

        if (client.getDisbursementsAmount() < 0 || client.getServicesAmount() < 0 || client.getAmount() < 0) {
            throw new ServiceException("Amounts cannot be negative");
        }

        // Validate that total amount matches sum of disbursements and services
        double expectedTotal = client.getDisbursementsAmount() + client.getServicesAmount();
        if (Math.abs(client.getAmount() - expectedTotal) > 0.001) {
            throw new ServiceException("Total amount must equal sum of disbursements and services amounts");
        }
    }

    /**
     * Validates that a client exists.
     * @param clientID The ID of the client to validate
     * @throws ServiceException if the client does not exist
     */
    private void validateClientExists(String clientID) throws ServiceException {
        Optional<Clients> client = getClientById(clientID);
        if (client.isEmpty()) {
            throw new ServiceException("Client not found with ID: " + clientID);
        }
    }

    /**
     * Validates that a client does not exist.
     * @param clientID The ID of the client to validate
     * @throws ServiceException if the client already exists
     */
    private void validateClientDoesNotExist(String clientID) throws ServiceException {
        Optional<Clients> client = getClientById(clientID);
        if (client.isPresent()) {
            throw new ServiceException("Client already exists with ID: " + clientID);
        }
    }
}
