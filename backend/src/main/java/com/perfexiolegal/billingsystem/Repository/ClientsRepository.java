package com.perfexiolegal.billingsystem.Repository;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Model.Clients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Repository
public class ClientsRepository {

  final Logger logger = LoggerFactory.getLogger(ClientsRepository.class);

  @Autowired
  JdbcTemplate jdbcTemplate;

  public Optional<List<Clients>> getAllClients() throws RepositoryException {
    try {
      String sql = "SELECT * FROM clients";
      logger.info("Retrieving data for all clients");
      List<Clients> clientsList = jdbcTemplate.query(sql,
          (resultSet, i) -> {
            String clientId = resultSet.getString("client_id");
            String clientName = resultSet.getString("client_name");
            String currencyCode = resultSet.getString("currency_code");
            float amount = resultSet.getFloat("amount");
            return Clients.builder().clientId(clientId).clientName(clientName).currencyCode(currencyCode)
                .amount(amount).build();
          });
      return Optional.of(clientsList);
    } catch (DataAccessException e) {
      throw new RepositoryException("Error in accessing the client in the repository", e);
    }
  }

  public Optional<Clients> getClientsById(String clientID) throws RepositoryException {
    try {
      String sql = "SELECT * FROM clients where client_id = '" + clientID + "'";
      logger.info("Retrieving data for client with client ID: " + clientID);
      Clients client = jdbcTemplate.queryForObject(sql,
          (resultSet, i) -> {
            String clientId = resultSet.getString("client_id");
            String clientName = resultSet.getString("client_name");
            String currencyCode = resultSet.getString("currency_code");
            float amount = resultSet.getFloat("amount");
            return Clients.builder().clientId(clientId).clientName(clientName).currencyCode(currencyCode)
                .amount(amount).build();
          });
      return Optional.of(client);
    } catch (DataAccessException e) {
      throw new RepositoryException("Error in accessing the client in the repository", e);
    }
  }

  public Clients postClients(Clients client) throws RepositoryException {
    try {
      logger.info("Creating client: " + client.getClientId());
      jdbcTemplate.update("insert into clients (client_id, client_name, currency_code, amount) values (?,?,?,?)",
          client.getClientId(), client.getClientName(), client.getCurrencyCode(), client.getAmount());
      return client;
    } catch (DataAccessException e){
      throw new RepositoryException("failed to insert client", e);
    }
  }

  public Clients updateClients(Clients client) throws RepositoryException {
    try {
      logger.info("Updating client: " + client.getClientId());
      jdbcTemplate.update("update clients set client_name=?, currency_code=?, amount=? " +
              "where client_id=?", client.getClientName(), client.getCurrencyCode(),
          client.getAmount(), client.getClientId());
      return client;
    } catch (DataAccessException e) {
      throw new RepositoryException("failed to query for client", e);
    }
  }

  public int deleteById(String clientID) throws RepositoryException {
    try {
      logger.info("Deleting client with ID: " + clientID);
      return jdbcTemplate.update("delete from clients where client_id=?", clientID);
    } catch (DataAccessException e) {
      throw new RepositoryException("Failed to delete client", e);
    }
  }

}
