package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.ClientDetails;
import com.perfexiolegal.billingsystem.Repository.IClientsRepository;
import com.perfexiolegal.billingsystem.Transformer.ClientsTransformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class ClientsService implements IClientsService {

    private static final Logger logger = LoggerFactory.getLogger(ClientsService.class);

    @Autowired
    private IClientsRepository clientsRepository;
    @Autowired
    private ClientsTransformer clientsTransformer;

    public Optional<List<ClientDetails>> getAll() throws ServiceException {
        try {
            logger.debug("Retrieving all clients");
            return clientsRepository.getAll();
        } catch (RepositoryException e) {
            logger.error("Error retrieving all clients: {}", e.getMessage());
            throw new ServiceException("Failed to retrieve clients", e);
        }
    }

    public Optional<ClientDetails> getById(String clientID) throws ServiceException {
        if (!StringUtils.hasText(clientID)) {
            throw new ServiceException("Client ID cannot be empty");
        }

        try {
            logger.debug("Retrieving client with ID: {}", clientID);
            return clientsRepository.getById(clientID);
        } catch (RepositoryException e) {
            logger.error("Error retrieving client with ID {}: {}", clientID, e.getMessage());
            throw new ServiceException("Failed to retrieve client", e);
        }
    }

    public void create(ClientDetails client) throws ServiceException {
        try {
            logger.debug("Creating new client with ID: {}", client.getClientId());
            clientsRepository.create(client);
        } catch (RepositoryException e) {
            logger.error("Error creating client: {}", e.getMessage());
            throw new ServiceException("Failed to create client", e);
        }
    }

    public void update(ClientDetails client) throws ServiceException {
        try {
            logger.debug("Updating client with ID: {}", client.getClientId());
            clientsRepository.update(client);
        } catch (RepositoryException e) {
            logger.error("Error updating client: {}", e.getMessage());
            throw new ServiceException("Failed to update client", e);
        }
    }

    public int deleteById(String clientID) throws ServiceException {
        if (!StringUtils.hasText(clientID)) {
            throw new ServiceException("Client ID cannot be empty");
        }

        try {
            logger.debug("Deleting client with ID: {}", clientID);
            return clientsRepository.deleteById(clientID);
        } catch (RepositoryException e) {
            logger.error("Error deleting client: {}", e.getMessage());
            throw new ServiceException("Failed to delete client", e);
        }
    }

    public ClientDetails updateAmounts(String clientID, double disbursementsAmount, double servicesAmount) 
            throws ServiceException {
        if (!StringUtils.hasText(clientID)) {
            throw new ServiceException("Client ID cannot be empty");
        }

        Optional<ClientDetails> existingClient = getById(clientID);
        if (existingClient.isEmpty()) {
            throw new ServiceException("Client not found with ID: " + clientID);
        }

        ClientDetails updatedClient = clientsTransformer.updateAmount(existingClient.get(), disbursementsAmount, servicesAmount);
        update(updatedClient);
        return updatedClient;
    }
}
