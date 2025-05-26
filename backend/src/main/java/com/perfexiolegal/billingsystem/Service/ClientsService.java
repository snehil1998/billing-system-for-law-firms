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

@Service
public class ClientsService {

    private static final Logger logger = LoggerFactory.getLogger(ClientsService.class);

    @Autowired
    private ClientsRepository clientsRepository;
    @Autowired
    private ClientsTransformer clientsTransformer;

    public Optional<List<Clients>> getAllClients() throws ServiceException {
        try {
            logger.debug("Retrieving all clients");
            return clientsRepository.getAllClients();
        } catch (RepositoryException e) {
            logger.error("Error retrieving all clients: {}", e.getMessage());
            throw new ServiceException("Failed to retrieve clients", e);
        }
    }

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

    public Clients updateAmounts(String clientID, double disbursementsAmount, double servicesAmount) 
            throws ServiceException {
        if (!StringUtils.hasText(clientID)) {
            throw new ServiceException("Client ID cannot be empty");
        }

        Optional<Clients> existingClient = getClientById(clientID);
        if (existingClient.isEmpty()) {
            throw new ServiceException("Client not found with ID: " + clientID);
        }

        Clients updatedClient = clientsTransformer.updateAmount(existingClient.get(), disbursementsAmount, servicesAmount);
        return updateClient(updatedClient);
    }

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

        // Validate that total amount matches sum of disbursements and services
        double expectedTotal = client.getDisbursementsAmount() + client.getServicesAmount();
        if (Math.abs(client.getAmount() - expectedTotal) > 0.001) {
            throw new ServiceException("Total amount must equal sum of disbursements and services amounts");
        }
    }

    private void validateClientExists(String clientID) throws ServiceException {
        Optional<Clients> client = getClientById(clientID);
        if (client.isEmpty()) {
            throw new ServiceException("Client not found with ID: " + clientID);
        }
    }

    private void validateClientDoesNotExist(String clientID) throws ServiceException {
        Optional<Clients> client = getClientById(clientID);
        if (client.isPresent()) {
            throw new ServiceException("Client already exists with ID: " + clientID);
        }
    }
}
