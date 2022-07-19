package com.perfexiolegal.billingsystem.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Model.Attorneys;
import com.perfexiolegal.billingsystem.Model.AttorneysInService;
import com.perfexiolegal.billingsystem.Model.ServicePricing;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Repository
public class AttorneysRepository {

  final Logger logger = LoggerFactory.getLogger(AttorneysRepository.class);

  @Autowired
  JdbcTemplate jdbcTemplate;

  public Optional<List<Attorneys>> getAllAttorneys() throws RepositoryException {
    try {
      String sql = "SELECT * FROM attorneys";
      logger.info("Retrieving data for all attorneys");
      List<Attorneys> attorneysList = jdbcTemplate.query(sql,
          (resultSet, i) -> {
            UUID attorneyId = (UUID) resultSet.getObject("attorney_id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            ObjectMapper objectMapper = new ObjectMapper();
            List<ServicePricing> servicePricing = null;
            try {
              servicePricing =
                  objectMapper.readValue(resultSet.getString("service_pricing"),
                      new TypeReference<List<ServicePricing>>(){});
            } catch (JsonProcessingException e) {
              logger.info("can't convert from PG json to java object");
            }
            return Attorneys.builder().attorneyId(attorneyId).firstName(firstName).lastName(lastName)
                .servicePricing(servicePricing).build();
          });
      return Optional.of(attorneysList);
    } catch (DataAccessException e) {
      throw new RepositoryException("Error in accessing the attorneys in the repository", e);
    }
  }

  public Optional<Attorneys> getAttorneyById(UUID attorneyID) throws RepositoryException {
    try {
      String sql = "SELECT * FROM attorneys where attorney_id = '" + attorneyID + "'";
      logger.info("Retrieving data for attorney with attorney ID: " + attorneyID);
      Attorneys attorney = jdbcTemplate.queryForObject(sql,
          (resultSet, i) -> {
            UUID attorneyId = (UUID) resultSet.getObject("attorney_id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType type = objectMapper.getTypeFactory().constructParametricType(List.class, ServicePricing.class);
            List<ServicePricing> servicePricing = null;
            try {
              servicePricing =
                  objectMapper.readValue(resultSet.getString("service_pricing"),
                      type);
            } catch (JsonProcessingException e) {
              logger.info("cannot convert PG object to service pricing object: ", e);
            }
            return Attorneys.builder().attorneyId(attorneyId).firstName(firstName).lastName(lastName)
                .servicePricing(servicePricing).build();
          });
      return Optional.of(attorney);
    } catch (DataAccessException e) {
      throw new RepositoryException("Error in accessing the attorney in the repository", e);
    }
  }

  public Attorneys postAttorneys(Attorneys attorneys) throws RepositoryException {
    try {
      logger.info("Creating attorney: " + attorneys.getAttorneyId());
      ObjectMapper objectMapper = new ObjectMapper();
      PGobject jsonObject = new PGobject();
      jsonObject.setType("json");
      jsonObject.setValue(objectMapper.writeValueAsString(attorneys.getServicePricing()));
      jdbcTemplate.update("insert into attorneys (attorney_id, first_name, last_name, service_pricing) " +
              "values (?,?,?,?)",
          attorneys.getAttorneyId(), attorneys.getFirstName(), attorneys.getLastName(), jsonObject);
      return attorneys;
    } catch (DataAccessException | JsonProcessingException | SQLException e){
      throw new RepositoryException("failed to insert attorney", e);
    }
  }

  public Attorneys updateAttorneys(Attorneys attorneys) throws RepositoryException {
    try {
      logger.info("Updating attorney: " + attorneys.getAttorneyId());
      ObjectMapper objectMapper = new ObjectMapper();
      PGobject jsonObject = new PGobject();
      jsonObject.setType("json");
      jsonObject.setValue(objectMapper.writeValueAsString(attorneys.getServicePricing()));
      jdbcTemplate.update("update attorneys set first_name=?, last_name=?, service_pricing=? " +
              "where attorney_id=?", attorneys.getFirstName(), attorneys.getLastName(), attorneys.getServicePricing(),
          attorneys.getAttorneyId());
      return attorneys;
    } catch (DataAccessException | JsonProcessingException | SQLException e) {
      throw new RepositoryException("failed to query for attorney", e);
    }
  }

  public int deleteById(UUID attorneyID) throws RepositoryException {
    try {
      logger.info("Deleting attorney with ID: " + attorneyID);
      return jdbcTemplate.update("delete from attorneys where attorney_id=?", attorneyID);
    } catch (DataAccessException e) {
      throw new RepositoryException("Failed to delete attorney", e);
    }
  }

}
