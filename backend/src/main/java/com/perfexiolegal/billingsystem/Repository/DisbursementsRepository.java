package com.perfexiolegal.billingsystem.Repository;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Model.Disbursements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Repository
public class DisbursementsRepository {

  final Logger logger = LoggerFactory.getLogger(DisbursementsRepository.class);

  @Autowired
  JdbcTemplate jdbcTemplate;

  public Optional<List<Disbursements>> getAllDisbursements() throws RepositoryException {
    try {
      String sql = "SELECT * FROM ebdb.public.disbursements";
      logger.info("Retrieving data for all disbursements");
      List<Disbursements> disbursementList = jdbcTemplate.query(sql,
          (resultSet, i) -> {
            String disbursementId = resultSet.getString("disbursement_id");
            String caseId = resultSet.getString("case_id");
            String clientId = resultSet.getString("client_id");
            String disbursement = resultSet.getString("disbursement");
            Date date = (Date) resultSet.getObject("date");
            String currencyCode = resultSet.getString("currency_code");
            float conversionRate = resultSet.getFloat("conversion_rate");
            float inrAmount = resultSet.getFloat("inr_amount");
            float conversionAmount = resultSet.getFloat("conversion_amount");
            return Disbursements.builder().disbursementId(disbursementId).caseId(caseId).clientId(clientId).disbursement(disbursement)
                .date(date).currencyCode(currencyCode).conversionRate(conversionRate).inrAmount(inrAmount)
                .conversionAmount(conversionAmount).build();
          });
      return Optional.of(disbursementList);
    } catch (DataAccessException e) {
      throw new RepositoryException("Error in accessing the disbursements in the repository", e);
    }
  }

  public Optional<Disbursements> getDisbursementsById(String disbursementId) throws RepositoryException {
    try {
      String sql = "SELECT * FROM ebdb.public.disbursements where disbursement_id = '" + disbursementId + "'";
      logger.info("Retrieving data for disbursement with disbursement ID: " + disbursementId);
      Disbursements disbursements = jdbcTemplate.queryForObject(sql,
          (resultSet, i) -> {
            String caseId = resultSet.getString("case_id");
            String clientId = resultSet.getString("client_id");
            String disbursement = resultSet.getString("disbursement");
            Date date = (Date) resultSet.getObject("date");
            String currencyCode = resultSet.getString("currency_code");
            float conversionRate = resultSet.getFloat("conversion_rate");
            float inrAmount = resultSet.getFloat("inr_amount");
            float conversionAmount = resultSet.getFloat("conversion_amount");
            return Disbursements.builder().disbursementId(disbursementId).caseId(caseId).clientId(clientId).disbursement(disbursement)
                .date(date).currencyCode(currencyCode).conversionRate(conversionRate).inrAmount(inrAmount)
                .conversionAmount(conversionAmount).build();
          });
      return Optional.of(disbursements);
    } catch (DataAccessException e) {
      throw new RepositoryException("Error in accessing the disbursement in the repository", e);
    }
  }

  public Optional<List<Disbursements>> getDisbursementsByClientId(String clientID) throws RepositoryException {
    try {
      String sql = "SELECT * FROM ebdb.public.disbursements where client_id = '" + clientID + "'";
      logger.info("Retrieving data for disbursements with client ID: " + clientID);
      List<Disbursements> disbursementList = jdbcTemplate.query(sql,
          (resultSet, i) -> {
            String disbursementId = resultSet.getString("disbursement_id");
            String caseId = resultSet.getString("case_id");
            String clientId = resultSet.getString("client_id");
            String disbursement = resultSet.getString("disbursement");
            Date date = (Date) resultSet.getObject("date");
            String currencyCode = resultSet.getString("currency_code");
            float conversionRate = resultSet.getFloat("conversion_rate");
            float inrAmount = resultSet.getFloat("inr_amount");
            float conversionAmount = resultSet.getFloat("conversion_amount");
            return Disbursements.builder().disbursementId(disbursementId).caseId(caseId).clientId(clientId).disbursement(disbursement)
                .date(date).currencyCode(currencyCode).conversionRate(conversionRate).inrAmount(inrAmount)
                .conversionAmount(conversionAmount).build();
          });
      return Optional.of(disbursementList);
    } catch (DataAccessException e) {
      throw new RepositoryException("Error in accessing the disbursements in the repository", e);
    }
  }

  public Optional<List<Disbursements>> getDisbursementsByCaseId(String caseID) throws RepositoryException {
    try {
      String sql = "SELECT * FROM ebdb.public.disbursements where case_id = '" + caseID + "'";
      logger.info("Retrieving data for disbursements with case ID: " + caseID);
      List<Disbursements> disbursementList = jdbcTemplate.query(sql,
          (resultSet, i) -> {
            String disbursementId = resultSet.getString("disbursement_id");
            String caseId = resultSet.getString("case_id");
            String clientId = resultSet.getString("client_id");
            String disbursement = resultSet.getString("disbursement");
            Date date = (Date) resultSet.getObject("date");
            String currencyCode = resultSet.getString("currency_code");
            float conversionRate = resultSet.getFloat("conversion_rate");
            float inrAmount = resultSet.getFloat("inr_amount");
            float conversionAmount = resultSet.getFloat("conversion_amount");
            return Disbursements.builder().disbursementId(disbursementId).caseId(caseId).clientId(clientId).disbursement(disbursement)
                .date(date).currencyCode(currencyCode).conversionRate(conversionRate).inrAmount(inrAmount)
                .conversionAmount(conversionAmount).build();
          });
      return Optional.of(disbursementList);
    } catch (DataAccessException e) {
      throw new RepositoryException("Error in accessing the disbursements in the repository", e);
    }
  }

  public void postDisbursements(Disbursements disbursement) throws RepositoryException {
    try {
      logger.info("Creating disbursement: " + disbursement.getDisbursementId());
      jdbcTemplate.update("insert into ebdb.public.disbursements (disbursement_id, case_id, client_id, disbursement, date," +
              "currency_code, conversion_rate, inr_amount, conversion_amount) values (?,?,?,?,?,?,?,?,?)",
          disbursement.getDisbursementId(), disbursement.getCaseId(), disbursement.getClientId(), disbursement.getDisbursement(),
          disbursement.getDate(), disbursement.getCurrencyCode(), disbursement.getConversionRate(), disbursement.getInrAmount(),
          disbursement.getConversionAmount());
    } catch (DataAccessException e){
      throw new RepositoryException("failed to insert disbursement", e);
    }
  }

  public void updateDisbursements(Disbursements disbursement) throws RepositoryException {
    try {
      logger.info("Updating disbursement: " + disbursement.getDisbursementId());
      jdbcTemplate.update("update ebdb.public.disbursements set case_id=?, client_id=?, disbursement=?, date=?," +
              "currency_code=?, conversion_rate=?, inr_amount=?, conversion_amount=? " +
              "where disbursement_id=?", disbursement.getCaseId(), disbursement.getClientId(), disbursement.getDisbursement(),
          disbursement.getDate(), disbursement.getCurrencyCode(), disbursement.getConversionRate(), disbursement.getInrAmount(),
          disbursement.getConversionAmount(), disbursement.getDisbursementId());
    } catch (DataAccessException e) {
      throw new RepositoryException("failed to query for disbursement", e);
    }
  }

  public int deleteDisbursementById(String disbursementId) throws RepositoryException {
    try {
      logger.info("Deleting disbursement with ID: " + disbursementId);
      return jdbcTemplate.update("delete from ebdb.public.disbursements where disbursement_id=?", disbursementId);
    } catch (DataAccessException e) {
      throw new RepositoryException("Failed to delete disbursement", e);
    }
  }

}
