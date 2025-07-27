package com.perfexiolegal.billingsystem.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Model.AttorneyMinutes;
import com.perfexiolegal.billingsystem.Model.ServiceRequestDto;
import com.perfexiolegal.billingsystem.Model.ServiceResponse;

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

@Repository
public class ServicesRepository implements IServicesRepository {

    private static final Logger logger = LoggerFactory.getLogger(ServicesRepository.class);
    private static final String TABLE_NAME = "ebdb.public.services";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<ServiceResponse> servicesRowMapper = (resultSet, i) -> {
        Long serviceId = resultSet.getLong("service_id");
        String caseId = resultSet.getString("case_id");
        String clientId = resultSet.getString("client_id");
        String service = resultSet.getString("service");
        String discription = resultSet.getString("description");
        Date date = (Date) resultSet.getObject("date");
        List<AttorneyMinutes> attorneys = null;
        try {
            JavaType type = objectMapper.getTypeFactory().constructParametricType(List.class, AttorneyMinutes.class);
            attorneys = objectMapper.readValue(resultSet.getString("attorneys"), type);
        } catch (JsonProcessingException e) {
            logger.error("Failed to convert attorneys JSON to object: {}", e.getMessage());
        }
        float amount = resultSet.getFloat("amount");
        return ServiceResponse.builder()
                .serviceId(serviceId)
                .caseId(caseId)
                .clientId(clientId)
                .service(service)
                .description(discription)
                .date(date)
                .attorneys(attorneys)
                .amount(amount)
                .build();
    };

    public Optional<List<ServiceResponse>> getAll() throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME;
            logger.debug("Retrieving all services");
            List<ServiceResponse> servicesList = jdbcTemplate.query(sql, servicesRowMapper);
            return Optional.of(servicesList);
        } catch (DataAccessException e) {
            logger.error("Error retrieving all services: {}", e.getMessage());
            throw new RepositoryException("Failed to retrieve services from database", e);
        }
    }

    public Optional<List<ServiceResponse>> getByCaseId(String caseID) throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE case_id = ?";
            logger.debug("Retrieving services for case with ID: {}", caseID);
            List<ServiceResponse> servicesForCaseList = jdbcTemplate.query(sql, servicesRowMapper, caseID);
            return Optional.of(servicesForCaseList);
        } catch (DataAccessException e) {
            logger.error("Error retrieving services for case {}: {}", caseID, e.getMessage());
            throw new RepositoryException("Failed to retrieve services from database", e);
        }
    }

    public Optional<List<ServiceResponse>> getByClientId(String clientID) throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE client_id = ?";
            logger.debug("Retrieving services for client with ID: {}", clientID);
            List<ServiceResponse> servicesForClientList = jdbcTemplate.query(sql, servicesRowMapper, clientID);
            return Optional.of(servicesForClientList);
        } catch (DataAccessException e) {
            logger.error("Error retrieving services for client {}: {}", clientID, e.getMessage());
            throw new RepositoryException("Failed to retrieve services from database", e);
        }
    }

    public Optional<ServiceResponse> getById(Long serviceID) throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE service_id = ?";
            logger.debug("Retrieving service with ID: {}", serviceID);
            List<ServiceResponse> services = jdbcTemplate.query(sql, servicesRowMapper, serviceID);
            return services.isEmpty() ? Optional.empty() : Optional.of(services.get(0));
        } catch (DataAccessException e) {
            logger.error("Error retrieving service with ID {}: {}", serviceID, e.getMessage());
            throw new RepositoryException("Failed to retrieve service from database", e);
        }
    }

    public void create(ServiceRequestDto service) throws RepositoryException {
        try {
            String sql = "INSERT INTO " + TABLE_NAME + 
                    " (case_id, client_id, service, description, date, attorneys, amount) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            logger.debug("Creating service for case: {}", service.getCaseId());
            
            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(objectMapper.writeValueAsString(service.getAttorneys()));
            
            jdbcTemplate.update(sql,
                    service.getCaseId(),
                    service.getClientId(),
                    service.getService(),
                    service.getDescription(),
                    service.getDate(),
                    jsonObject,
                    service.getAmount());
        } catch (DataAccessException | JsonProcessingException | SQLException e) {
            logger.error("Error creating service: {}", e.getMessage());
            throw new RepositoryException("Failed to create service in database", e);
        }
    }

    public void update(Long serviceId, ServiceRequestDto service) throws RepositoryException {
        try {
            String sql = "UPDATE " + TABLE_NAME + 
                    " SET case_id = ?, client_id = ?, service = ?, " +
                    "description = ?, date = ?, attorneys = ?, amount = ? " +
                    "WHERE service_id = ?";
            logger.debug("Updating service with ID: {}", serviceId);
            
            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(objectMapper.writeValueAsString(service.getAttorneys()));
            
            jdbcTemplate.update(sql,
                    service.getCaseId(),
                    service.getClientId(),
                    service.getService(),
                    service.getDescription(),
                    service.getDate(),
                    jsonObject,
                    service.getAmount(),
                    serviceId);
        } catch (DataAccessException | JsonProcessingException | SQLException e) {
            logger.error("Error updating service: {}", e.getMessage());
            throw new RepositoryException("Failed to update service in database", e);
        }
    }

    public int deleteById(Long serviceID) throws RepositoryException {
        try {
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE service_id = ?";
            logger.debug("Deleting service with ID: {}", serviceID);
            return jdbcTemplate.update(sql, serviceID);
        } catch (DataAccessException e) {
            logger.error("Error deleting service: {}", e.getMessage());
            throw new RepositoryException("Failed to delete service from database", e);
        }
    }

    public int deleteByCaseId(String caseID) throws RepositoryException {
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
