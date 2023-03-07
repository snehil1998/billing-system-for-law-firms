package com.perfexiolegal.billingsystem.Repository;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Model.Cases;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public class CasesRepository {

  final Logger logger = LoggerFactory.getLogger(CasesRepository.class);

  @Autowired
  JdbcTemplate jdbcTemplate;

  public Optional<List<Cases>> getAllCases() throws RepositoryException {
    try {
      String sql = "SELECT * FROM ebdb.public.cases";
      logger.info("Retrieving data for all cases");
      List<Cases> casesList = jdbcTemplate.query(sql,
          (resultSet, i) -> {
            String caseId = resultSet.getString("case_id");
            String caseName = resultSet.getString("case_name");
            String clientId = resultSet.getString("client_id");
            String currencyCode = resultSet.getString("currency_code");
            float disbursementsAmount = resultSet.getFloat("disbursements_amount");
            float servicesAmount = resultSet.getFloat("services_amount");
            float amount = resultSet.getFloat("amount");
            return Cases.builder().caseId(caseId).caseName(caseName).clientId(clientId).currencyCode(currencyCode)
                .disbursementsAmount(disbursementsAmount).servicesAmount(servicesAmount).amount(amount).build();
          });
      return Optional.of(casesList);
    } catch (DataAccessException e) {
      throw new RepositoryException("Error in accessing the case in the repository", e);
    }
  }

  public Optional<Cases> getCaseById(String caseID) throws RepositoryException {
    try {
      String sql = "SELECT * FROM ebdb.public.cases where case_id = '" + caseID + "'";
      logger.info("Retrieving data for case with case ID: " + caseID);
      Cases caseList = jdbcTemplate.queryForObject(sql,
          (resultSet, i) -> {
            String caseId = resultSet.getString("case_id");
            String caseName = resultSet.getString("case_name");
            String clientId = resultSet.getString("client_id");
            String currencyCode = resultSet.getString("currency_code");
            float disbursementsAmount = resultSet.getFloat("disbursements_amount");
            float servicesAmount = resultSet.getFloat("services_amount");
            float amount = resultSet.getFloat("amount");
            return Cases.builder().caseId(caseId).caseName(caseName).clientId(clientId).currencyCode(currencyCode)
                .disbursementsAmount(disbursementsAmount).servicesAmount(servicesAmount).amount(amount).build();
          });
      return Optional.of(caseList);
    } catch (DataAccessException e) {
      throw new RepositoryException("Error in accessing the case in the repository", e);
    }
  }

  public Cases postCases(Cases postCase) throws RepositoryException {
    try {
      logger.info("Creating case: " + postCase.getCaseId());
      jdbcTemplate.update("insert into ebdb.public.cases (case_id, case_name, client_id, currency_code, disbursements_amount, services_amount, amount) values (?,?,?,?,?,?,?)",
          postCase.getCaseId(), postCase.getCaseName(), postCase.getClientId(), postCase.getCurrencyCode(), postCase.getDisbursementsAmount(), postCase.getServicesAmount(), postCase.getAmount());
      return postCase;
    } catch (DataAccessException e){
      throw new RepositoryException("failed to insert case", e);
    }
  }

  public Cases updateCases(Cases updatedCase) throws RepositoryException {
    try {
      logger.info("Updating case: " + updatedCase.getCaseId());
      jdbcTemplate.update("update ebdb.public.cases set case_name=?, client_id=?, currency_code=?, disbursements_amount=?, services_amount=?, amount=? " +
              "where case_id=?", updatedCase.getCaseName(), updatedCase.getClientId(), updatedCase.getCurrencyCode(), updatedCase.getDisbursementsAmount(),
          updatedCase.getServicesAmount(), updatedCase.getAmount(), updatedCase.getCaseId());
      return updatedCase;
    } catch (DataAccessException e) {
      throw new RepositoryException("failed to query for case", e);
    }
  }

  public int deleteById(String caseID) throws RepositoryException {
    try {
      logger.info("Deleting case with ID: " + caseID);
      return jdbcTemplate.update("delete from ebdb.public.cases where case_id=?", caseID);
    } catch (DataAccessException e) {
      throw new RepositoryException("Failed to delete case", e);
    }
  }

}
