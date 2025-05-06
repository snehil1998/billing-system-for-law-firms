package com.perfexiolegal.billingsystem.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Model.Attorneys;
import com.perfexiolegal.billingsystem.Model.ServicePricing;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class AttorneysRepository {

    private static final Logger logger = LoggerFactory.getLogger(AttorneysRepository.class);
    private static final String TABLE_NAME = "ebdb.public.attorneys";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final JavaType SERVICE_PRICING_TYPE = objectMapper.getTypeFactory()
            .constructParametricType(List.class, ServicePricing.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<List<Attorneys>> getAllAttorneys() throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME;
            logger.info("Retrieving data for all attorneys");

            List<Attorneys> attorneysList = jdbcTemplate.query(sql, (resultSet, i) -> {
                String attorneyId = resultSet.getString("attorney_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                List<ServicePricing> servicePricing = parseServicePricing(resultSet.getString("service_pricing"));

                return Attorneys.builder()
                        .attorneyId(attorneyId)
                        .firstName(firstName)
                        .lastName(lastName)
                        .servicePricing(servicePricing)
                        .build();
            });

            return Optional.of(attorneysList);
        } catch (DataAccessException e) {
            throw new RepositoryException("Error in accessing the attorneys in the repository", e);
        }
    }

    public Optional<Attorneys> getAttorneyById(String attorneyID) throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE attorney_id = ?";
            logger.info("Retrieving data for attorney with ID: {}", attorneyID);

            List<Attorneys> results = jdbcTemplate.query(sql, (resultSet, i) -> {
                String attorneyId = resultSet.getString("attorney_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                List<ServicePricing> servicePricing = parseServicePricing(resultSet.getString("service_pricing"));

                return Attorneys.builder()
                        .attorneyId(attorneyId)
                        .firstName(firstName)
                        .lastName(lastName)
                        .servicePricing(servicePricing)
                        .build();
            }, attorneyID);

            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (DataAccessException e) {
            logger.error("Error retrieving attorney with ID: {}", attorneyID, e);
            throw new RepositoryException("Error in accessing the attorney in the repository", e);
        }
    }

    public Attorneys postAttorneys(Attorneys attorney) throws RepositoryException {
        try {
            logger.info("Creating attorney: {}", attorney.getAttorneyId());
            
            PGobject jsonObject = createJsonObject(attorney.getServicePricing());
            String sql = "INSERT INTO " + TABLE_NAME + 
                    " (attorney_id, first_name, last_name, service_pricing) VALUES (?, ?, ?, ?)";

            jdbcTemplate.update(sql,
                    attorney.getAttorneyId(),
                    attorney.getFirstName(),
                    attorney.getLastName(),
                    jsonObject);

            return attorney;
        } catch (DataAccessException | JsonProcessingException | SQLException e) {
            throw new RepositoryException("Failed to insert attorney", e);
        }
    }

    public Attorneys updateAttorneys(Attorneys attorney) throws RepositoryException {
        try {
            logger.info("Updating attorney: {}", attorney.getAttorneyId());
            
            PGobject jsonObject = createJsonObject(attorney.getServicePricing());
            String sql = "UPDATE " + TABLE_NAME + 
                    " SET first_name = ?, last_name = ?, service_pricing = ? WHERE attorney_id = ?";

            jdbcTemplate.update(sql,
                    attorney.getFirstName(),
                    attorney.getLastName(),
                    jsonObject,
                    attorney.getAttorneyId());

            return attorney;
        } catch (DataAccessException | JsonProcessingException | SQLException e) {
            throw new RepositoryException("Failed to query for attorney", e);
        }
    }

    public int deleteById(String attorneyID) throws RepositoryException {
        try {
            logger.info("Deleting attorney with ID: {}", attorneyID);
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE attorney_id = ?";
            return jdbcTemplate.update(sql, attorneyID);
        } catch (DataAccessException e) {
            throw new RepositoryException("Failed to delete attorney", e);
        }
    }

    private List<ServicePricing> parseServicePricing(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, SERVICE_PRICING_TYPE);
        } catch (JsonProcessingException e) {
            logger.info("Cannot convert PG object to service pricing object: {}", e.getMessage());
            return null;
        }
    }

    private PGobject createJsonObject(List<ServicePricing> servicePricing) throws SQLException, JsonProcessingException {
        PGobject jsonObject = new PGobject();
        jsonObject.setType("json");
        jsonObject.setValue(objectMapper.writeValueAsString(servicePricing));
        return jsonObject;
    }
}
