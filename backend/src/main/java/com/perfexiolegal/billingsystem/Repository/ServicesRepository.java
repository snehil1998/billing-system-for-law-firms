package com.perfexiolegal.billingsystem.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Model.AttorneysInService;
import com.perfexiolegal.billingsystem.Model.Services;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for handling service-related database operations.
 * Provides methods for CRUD operations on services.
 */
@Repository
public class ServicesRepository {

    private static final Logger logger = LoggerFactory.getLogger(ServicesRepository.class);
    private static final String TABLE_NAME = "ebdb.public.services";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Services> servicesRowMapper = (resultSet, i) -> {
        String serviceId = resultSet.getString("service_id");
        String caseId = resultSet.getString("case_id");
        String clientId = resultSet.getString("client_id");
        String service = resultSet.getString("service");
        Date date = (Date) resultSet.getObject("date");
        List<AttorneysInService> attorneys = null;
        try {
            JavaType type = objectMapper.getTypeFactory().constructParametricType(List.class, AttorneysInService.class);
            attorneys = objectMapper.readValue(resultSet.getString("attorneys"), type);
        } catch (JsonProcessingException e) {
            logger.error("Failed to convert attorneys JSON to object: {}", e.getMessage());
        }
        float amount = resultSet.getFloat("amount");
        return Services.builder()
                .serviceId(serviceId)
                .caseId(caseId)
                .clientId(clientId)
                .service(service)
                .date(date)
                .attorneys(attorneys)
                .amount(amount)
                .build();
    };

    /**
     * Retrieves all services from the database.
     * @return Optional containing a list of all services, or empty if none found
     * @throws RepositoryException if there is an error accessing the database
     */
    public Optional<List<Services>> getAllServices() throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME;
            logger.debug("Retrieving all services");
            List<Services> servicesList = jdbcTemplate.query(sql, servicesRowMapper);
            return Optional.of(servicesList);
        } catch (DataAccessException e) {
            logger.error("Error retrieving all services: {}", e.getMessage());
            throw new RepositoryException("Failed to retrieve services from database", e);
        }
    }

    /**
     * Retrieves all services for a specific case.
     * @param caseID The ID of the case
     * @return Optional containing a list of services for the case, or empty if none found
     * @throws RepositoryException if there is an error accessing the database
     */
    public Optional<List<Services>> getServicesForCase(String caseID) throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE case_id = ?";
            logger.debug("Retrieving services for case with ID: {}", caseID);
            List<Services> servicesForCaseList = jdbcTemplate.query(sql, servicesRowMapper, caseID);
            return Optional.of(servicesForCaseList);
        } catch (DataAccessException e) {
            logger.error("Error retrieving services for case {}: {}", caseID, e.getMessage());
            throw new RepositoryException("Failed to retrieve services from database", e);
        }
    }

    /**
     * Retrieves all services for a specific client.
     * @param clientID The ID of the client
     * @return Optional containing a list of services for the client, or empty if none found
     * @throws RepositoryException if there is an error accessing the database
     */
    public Optional<List<Services>> getServicesForClient(String clientID) throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE client_id = ?";
            logger.debug("Retrieving services for client with ID: {}", clientID);
            List<Services> servicesForClientList = jdbcTemplate.query(sql, servicesRowMapper, clientID);
            return Optional.of(servicesForClientList);
        } catch (DataAccessException e) {
            logger.error("Error retrieving services for client {}: {}", clientID, e.getMessage());
            throw new RepositoryException("Failed to retrieve services from database", e);
        }
    }

    /**
     * Retrieves a specific service by ID.
     * @param serviceID The ID of the service to retrieve
     * @return Optional containing the service if found, empty otherwise
     * @throws RepositoryException if there is an error accessing the database
     */
    public Optional<Services> getServiceFromId(String serviceID) throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE service_id = ?";
            logger.debug("Retrieving service with ID: {}", serviceID);
            List<Services> services = jdbcTemplate.query(sql, servicesRowMapper, serviceID);
            return services.isEmpty() ? Optional.empty() : Optional.of(services.get(0));
        } catch (DataAccessException e) {
            logger.error("Error retrieving service with ID {}: {}", serviceID, e.getMessage());
            throw new RepositoryException("Failed to retrieve service from database", e);
        }
    }

    /**
     * Creates a new service in the database.
     * @param service The service to create
     * @return The created service
     * @throws RepositoryException if there is an error accessing the database
     */
    public Services postServices(Services service) throws RepositoryException {
        try {
            String sql = "INSERT INTO " + TABLE_NAME + 
                    " (service_id, case_id, client_id, service, date, attorneys, amount) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            logger.debug("Creating service with ID: {}", service.getServiceId());
            
            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(objectMapper.writeValueAsString(service.getAttorneys()));
            
            jdbcTemplate.update(sql,
                    service.getServiceId(),
                    service.getCaseId(),
                    service.getClientId(),
                    service.getService(),
                    service.getDate(),
                    jsonObject,
                    service.getAmount());
            return service;
        } catch (DataAccessException | JsonProcessingException | SQLException e) {
            logger.error("Error creating service: {}", e.getMessage());
            throw new RepositoryException("Failed to create service in database", e);
        }
    }

    /**
     * Updates an existing service in the database.
     * @param service The service data to update
     * @return The updated service
     * @throws RepositoryException if there is an error accessing the database
     */
    public Services updateServices(Services service) throws RepositoryException {
        try {
            String sql = "UPDATE " + TABLE_NAME + 
                    " SET case_id = ?, client_id = ?, service = ?, " +
                    "date = ?, attorneys = ?, amount = ? " +
                    "WHERE service_id = ?";
            logger.debug("Updating service with ID: {}", service.getServiceId());
            
            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(objectMapper.writeValueAsString(service.getAttorneys()));
            
            jdbcTemplate.update(sql,
                    service.getCaseId(),
                    service.getClientId(),
                    service.getService(),
                    service.getDate(),
                    jsonObject,
                    service.getAmount(),
                    service.getServiceId());
            return service;
        } catch (DataAccessException | JsonProcessingException | SQLException e) {
            logger.error("Error updating service: {}", e.getMessage());
            throw new RepositoryException("Failed to update service in database", e);
        }
    }

    /**
     * Deletes a service from the database.
     * @param serviceID The ID of the service to delete
     * @return The number of rows affected (1 if successful, 0 if not found)
     * @throws RepositoryException if there is an error accessing the database
     */
    public int deleteById(String serviceID) throws RepositoryException {
        try {
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE service_id = ?";
            logger.debug("Deleting service with ID: {}", serviceID);
            return jdbcTemplate.update(sql, serviceID);
        } catch (DataAccessException e) {
            logger.error("Error deleting service: {}", e.getMessage());
            throw new RepositoryException("Failed to delete service from database", e);
        }
    }

    /**
     * Deletes all services for a specific case.
     * @param caseID The ID of the case
     * @return The number of rows affected
     * @throws RepositoryException if there is an error accessing the database
     */
    public int deleteByCase(String caseID) throws RepositoryException {
        try {
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE case_id = ?";
            logger.debug("Deleting services for case with ID: {}", caseID);
            return jdbcTemplate.update(sql, caseID);
        } catch (DataAccessException e) {
            logger.error("Error deleting services for case {}: {}", caseID, e.getMessage());
            throw new RepositoryException("Failed to delete services from database", e);
        }
    }
}
