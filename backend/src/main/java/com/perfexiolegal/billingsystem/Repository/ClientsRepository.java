package com.perfexiolegal.billingsystem.Repository;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Model.Clients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClientsRepository {

    private static final Logger logger = LoggerFactory.getLogger(ClientsRepository.class);
    private static final String TABLE_NAME = "ebdb.public.clients";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Clients> clientRowMapper = (resultSet, i) -> Clients.builder()
            .clientId(resultSet.getString("client_id"))
            .clientName(resultSet.getString("client_name"))
            .currencyCode(resultSet.getString("currency_code"))
            .disbursementsAmount(resultSet.getDouble("disbursements_amount"))
            .servicesAmount(resultSet.getDouble("services_amount"))
            .amount(resultSet.getDouble("amount"))
            .build();

    public Optional<List<Clients>> getAllClients() throws RepositoryException {
        try {
            logger.debug("Retrieving all clients from database");
            String sql = String.format("SELECT * FROM %s", TABLE_NAME);
            List<Clients> clientsList = jdbcTemplate.query(sql, clientRowMapper);
            return Optional.of(clientsList);
        } catch (DataAccessException e) {
            logger.error("Error retrieving all clients: {}", e.getMessage());
            throw new RepositoryException("Failed to retrieve clients from database", e);
        }
    }

    public Optional<Clients> getClientsById(String clientID) throws RepositoryException {
        try {
            logger.debug("Retrieving client with ID: {}", clientID);
            String sql = String.format("SELECT * FROM %s WHERE client_id = ?", TABLE_NAME);
            List<Clients> results = jdbcTemplate.query(sql, clientRowMapper, clientID);
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (DataAccessException e) {
            logger.error("Error retrieving client with ID {}: {}", clientID, e.getMessage());
            throw new RepositoryException("Failed to retrieve client from database", e);
        }
    }

    public Clients postClients(Clients client) throws RepositoryException {
        try {
            logger.debug("Creating new client with ID: {}", client.getClientId());
            String sql = String.format(
                "INSERT INTO %s (client_id, client_name, currency_code, disbursements_amount, services_amount, amount) " +
                "VALUES (?, ?, ?, ?, ?, ?)", 
                TABLE_NAME
            );
            
            jdbcTemplate.update(sql,
                client.getClientId(),
                client.getClientName(),
                client.getCurrencyCode(),
                client.getDisbursementsAmount(),
                client.getServicesAmount(),
                client.getAmount()
            );
            
            return client;
        } catch (DataAccessException e) {
            logger.error("Error creating client: {}", e.getMessage());
            throw new RepositoryException("Failed to create client in database", e);
        }
    }

    public Clients updateClients(Clients client) throws RepositoryException {
        try {
            logger.debug("Updating client with ID: {}", client.getClientId());
            String sql = String.format(
                "UPDATE %s SET client_name = ?, currency_code = ?, disbursements_amount = ?, " +
                "services_amount = ?, amount = ? WHERE client_id = ?",
                TABLE_NAME
            );
            
            jdbcTemplate.update(sql,
                client.getClientName(),
                client.getCurrencyCode(),
                client.getDisbursementsAmount(),
                client.getServicesAmount(),
                client.getAmount(),
                client.getClientId()
            );
            
            return client;
        } catch (DataAccessException e) {
            logger.error("Error updating client: {}", e.getMessage());
            throw new RepositoryException("Failed to update client in database", e);
        }
    }

    public int deleteById(String clientID) throws RepositoryException {
        try {
            logger.debug("Deleting client with ID: {}", clientID);
            String sql = String.format("DELETE FROM %s WHERE client_id = ?", TABLE_NAME);
            return jdbcTemplate.update(sql, clientID);
        } catch (DataAccessException e) {
            logger.error("Error deleting client: {}", e.getMessage());
            throw new RepositoryException("Failed to delete client from database", e);
        }
    }
}
