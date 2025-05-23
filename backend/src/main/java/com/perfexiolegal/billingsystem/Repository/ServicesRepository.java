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
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Repository
public class ServicesRepository {

  final Logger logger = LoggerFactory.getLogger(ServicesRepository.class);

  @Autowired
  JdbcTemplate jdbcTemplate;

  public Optional<List<Services>> getAllServices() throws RepositoryException {
    try {
      String sql = "SELECT * FROM ebdb.public.services";
      logger.info("Retrieving data for all bills");
      List<Services> servicesList = jdbcTemplate.query(sql,
          (resultSet, i) -> {
            String serviceId = resultSet.getString("service_id");
            String caseId = resultSet.getString("case_id");
            String clientId = resultSet.getString("client_id");
            String service = resultSet.getString("service");
            Date date = (Date) resultSet.getObject("date");
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType type = objectMapper.getTypeFactory().constructParametricType(List.class, AttorneysInService.class);
            List<AttorneysInService> attorneys = null;
            try {
              attorneys =
                  objectMapper.readValue(resultSet.getString("attorneys"), type);
            } catch (JsonProcessingException e) {
              logger.info("can't convert from PG json to java object");
            }
            float amount = resultSet.getFloat("amount");
            return Services.builder().serviceId(serviceId).caseId(caseId)
                .clientId(clientId).service(service).date(date)
                .attorneys(attorneys).amount(amount).build();
          });
      return Optional.of(servicesList);
    } catch (DataAccessException e) {
      throw new RepositoryException("Error in accessing the products in the repository", e);
    }
  }

  public Optional<List<Services>> getServicesForCase(String caseID) throws RepositoryException {
    try {
      String sql = "SELECT * FROM ebdb.public.services where case_id = '" + caseID + "'";
      logger.info("Retrieving data for bill with case ID: " + caseID);
      List<Services> servicesForCaseList = jdbcTemplate.query(sql,
          (resultSet, i) -> {
            String serviceId = resultSet.getString("service_id");
            String caseId = resultSet.getString("case_id");
            String clientId = resultSet.getString("client_id");
            String service = resultSet.getString("service");
            Date date = (Date) resultSet.getObject("date");
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType type = objectMapper.getTypeFactory().constructParametricType(List.class, AttorneysInService.class);
            List<AttorneysInService> attorneys = null;
            try {
              attorneys =
                  objectMapper.readValue(resultSet.getString("attorneys"), type);
              logger.info("attorneys: {}", attorneys);
            } catch (JsonProcessingException e) {
              logger.info("can't convert from PG json to java object");
            }
            //            int minutes = resultSet.getInt("minutes");
            float amount = resultSet.getFloat("amount");
            return Services.builder().serviceId(serviceId).caseId(caseId)
                .clientId(clientId).service(service).date(date)
                .attorneys(attorneys).amount(amount).build();
          });
      return Optional.of(servicesForCaseList);
    } catch (DataAccessException e) {
      throw new RepositoryException("Error in accessing the products in the repository", e);
    }
  }

  public Optional<List<Services>> getServicesForClient(String clientID) throws RepositoryException {
    try {
      String sql = "SELECT * FROM ebdb.public.services where client_id = '" + clientID + "'";
      logger.info("Retrieving data for bill with client ID: " + clientID);
      List<Services> servicesForClientList = jdbcTemplate.query(sql,
          (resultSet, i) -> {
            String serviceId = resultSet.getString("service_id");
            String caseId = resultSet.getString("case_id");
            String clientId = resultSet.getString("client_id");
            String service = resultSet.getString("service");
            Date date = (Date) resultSet.getObject("date");
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType type = objectMapper.getTypeFactory().constructParametricType(List.class, AttorneysInService.class);
            List<AttorneysInService> attorneys = null;
            try {
              attorneys =
                  objectMapper.readValue(resultSet.getString("attorneys"), type);
              logger.info("attorneys: {}", attorneys);
            } catch (JsonProcessingException e) {
              logger.info("can't convert from PG json to java object");
            }
            //            int minutes = resultSet.getInt("minutes");
            float amount = resultSet.getFloat("amount");
            return Services.builder().serviceId(serviceId).caseId(caseId)
                .clientId(clientId).service(service).date(date)
                .attorneys(attorneys).amount(amount).build();
          });
      return Optional.of(servicesForClientList);
    } catch (DataAccessException e) {
      throw new RepositoryException("Error in accessing the products in the repository", e);
    }
  }

  public Optional<Services> getServiceFromId(String serviceID) throws RepositoryException {
    try {
      String sql = "SELECT * FROM ebdb.public.services where service_id = '" + serviceID + "'";
      logger.info("Retrieving data for bill with service ID: " + serviceID);
      Services serviceFromId = jdbcTemplate.queryForObject(sql,
          (resultSet, i) -> {
            String serviceId = resultSet.getString("service_id");
            String caseId = resultSet.getString("case_id");
            String clientId = resultSet.getString("client_id");
            String service = resultSet.getString("service");
            Date date = (Date) resultSet.getObject("date");
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType type = objectMapper.getTypeFactory().constructParametricType(List.class, AttorneysInService.class);
            List<AttorneysInService> attorneys = null;
            try {
              attorneys =
                  objectMapper.readValue(resultSet.getString("attorneys"), type);
            } catch (JsonProcessingException e) {
              logger.info("can't convert from PG json to java object");
            }
            float amount = resultSet.getFloat("amount");
            return Services.builder().serviceId(serviceId).caseId(caseId)
                .clientId(clientId).service(service).date(date)
                .attorneys(attorneys).amount(amount).build();
          });
      return Optional.of(serviceFromId);
    } catch (DataAccessException e) {
      throw new RepositoryException("Error in accessing the products in the repository", e);
    }
  }

  public Services postServices(Services service) throws RepositoryException {
    try {
      logger.info("Creating service: " + service.getService());
      ObjectMapper objectMapper = new ObjectMapper();
      PGobject jsonObject = new PGobject();
      jsonObject.setType("json");
      jsonObject.setValue(objectMapper.writeValueAsString(service.getAttorneys()));
      logger.info("Creating service with attorneys: " + jsonObject);
      jdbcTemplate.update("insert into ebdb.public.services (Service_Id, Case_Id, Client_Id, Service, Date, Attorneys, Amount) values (?,?,?,?,?,?,?)",
          service.getServiceId(), service.getCaseId(), service.getClientId(), service.getService(),
          service.getDate(), jsonObject, service.getAmount());
      return service;
    } catch (DataAccessException | JsonProcessingException | SQLException e){
      throw new RepositoryException("failed to insert service", e);
    }
  }

  public Services updateServices(Services service) throws RepositoryException {
    try {
      logger.info("Updating service: " + service.getService());
      ObjectMapper objectMapper = new ObjectMapper();
      PGobject jsonObject = new PGobject();
      jsonObject.setType("json");
      jsonObject.setValue(objectMapper.writeValueAsString(service.getAttorneys()));
      jdbcTemplate.update("update ebdb.public.services set case_id=?, client_id=?, service=?, " +
              "date=?, attorneys=?, amount=? where service_id=?", service.getCaseId(), service.getClientId(),
          service.getService(), service.getDate(), jsonObject,
          service.getAmount(), service.getServiceId());
      return service;
    } catch (DataAccessException | JsonProcessingException | SQLException e) {
      throw new RepositoryException("failed to query for service", e);
    }
  }

  public int deleteById(String serviceID) throws RepositoryException {
    try {
      logger.info("Deleting service with ID: " + serviceID);
      return jdbcTemplate.update("delete from ebdb.public.services where service_id=?", serviceID);
    } catch (DataAccessException e) {
      throw new RepositoryException("Failed to delete task", e);
    }
  }

  public int deleteByCase(String caseID) throws RepositoryException {
    try {
      logger.info("Deleting services with case ID: " + caseID);
      return jdbcTemplate.update("delete from ebdb.public.services where case_id=?", caseID);
    } catch (DataAccessException e) {
      throw new RepositoryException("failed to delete tasks", e);
    }
  }
}
