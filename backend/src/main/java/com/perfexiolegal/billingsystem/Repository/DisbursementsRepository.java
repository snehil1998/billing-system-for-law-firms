package com.perfexiolegal.billingsystem.Repository;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Model.DisbursementDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class DisbursementsRepository {

    private static final Logger logger = LoggerFactory.getLogger(DisbursementsRepository.class);
    private static final String TABLE_NAME = "ebdb.public.disbursements";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<DisbursementDetails> disbursementsRowMapper = (resultSet, i) -> {
        String disbursementId = resultSet.getString("disbursement_id");
        String caseId = resultSet.getString("case_id");
        String clientId = resultSet.getString("client_id");
        String disbursement = resultSet.getString("disbursement");
        Date date = (Date) resultSet.getObject("date");
        String currencyCode = resultSet.getString("currency_code");
        double conversionRate = resultSet.getDouble("conversion_rate");
        double inrAmount = resultSet.getDouble("inr_amount");
        double conversionAmount = resultSet.getDouble("conversion_amount");
        return DisbursementDetails.builder()
                .disbursementId(disbursementId)
                .caseId(caseId)
                .clientId(clientId)
                .disbursement(disbursement)
                .date(date)
                .currencyCode(currencyCode)
                .conversionRate(conversionRate)
                .inrAmount(inrAmount)
                .conversionAmount(conversionAmount)
                .build();
    };

    public Optional<List<DisbursementDetails>> getAllDisbursements() throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME;
            logger.debug("Retrieving all disbursements");
            List<DisbursementDetails> disbursementList = jdbcTemplate.query(sql, disbursementsRowMapper);
            return Optional.of(disbursementList);
        } catch (DataAccessException e) {
            logger.error("Error retrieving all disbursements: {}", e.getMessage());
            throw new RepositoryException("Failed to retrieve disbursements from database", e);
        }
    }

    public Optional<DisbursementDetails> getDisbursementsById(String disbursementId) throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE disbursement_id = ?";
            logger.debug("Retrieving disbursement with ID: {}", disbursementId);
            List<DisbursementDetails> disbursements = jdbcTemplate.query(sql, disbursementsRowMapper, disbursementId);
            return disbursements.isEmpty() ? Optional.empty() : Optional.of(disbursements.get(0));
        } catch (DataAccessException e) {
            logger.error("Error retrieving disbursement with ID {}: {}", disbursementId, e.getMessage());
            throw new RepositoryException("Failed to retrieve disbursement from database", e);
        }
    }

    public Optional<List<DisbursementDetails>> getDisbursementsByClientId(String clientID) throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE client_id = ?";
            logger.debug("Retrieving disbursements for client with ID: {}", clientID);
            List<DisbursementDetails> disbursementList = jdbcTemplate.query(sql, disbursementsRowMapper, clientID);
            return Optional.of(disbursementList);
        } catch (DataAccessException e) {
            logger.error("Error retrieving disbursements for client {}: {}", clientID, e.getMessage());
            throw new RepositoryException("Failed to retrieve disbursements from database", e);
        }
    }

    public Optional<List<DisbursementDetails>> getDisbursementsByCaseId(String caseID) throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE case_id = ?";
            logger.debug("Retrieving disbursements for case with ID: {}", caseID);
            List<DisbursementDetails> disbursementList = jdbcTemplate.query(sql, disbursementsRowMapper, caseID);
            return Optional.of(disbursementList);
        } catch (DataAccessException e) {
            logger.error("Error retrieving disbursements for case {}: {}", caseID, e.getMessage());
            throw new RepositoryException("Failed to retrieve disbursements from database", e);
        }
    }

    public void postDisbursements(DisbursementDetails disbursement) throws RepositoryException {
        try {
            String sql = "INSERT INTO " + TABLE_NAME + 
                    " (disbursement_id, case_id, client_id, disbursement, date, " +
                    "currency_code, conversion_rate, inr_amount, conversion_amount) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            logger.debug("Creating disbursement with ID: {}", disbursement.getDisbursementId());
            
            jdbcTemplate.update(sql,
                    disbursement.getDisbursementId(),
                    disbursement.getCaseId(),
                    disbursement.getClientId(),
                    disbursement.getDisbursement(),
                    disbursement.getDate(),
                    disbursement.getCurrencyCode(),
                    disbursement.getConversionRate(),
                    disbursement.getInrAmount(),
                    disbursement.getConversionAmount());
        } catch (DataAccessException e) {
            logger.error("Error creating disbursement: {}", e.getMessage());
            throw new RepositoryException("Failed to create disbursement in database", e);
        }
    }

    public void updateDisbursements(DisbursementDetails disbursement) throws RepositoryException {
        try {
            String sql = "UPDATE " + TABLE_NAME + 
                    " SET case_id = ?, client_id = ?, disbursement = ?, date = ?, " +
                    "currency_code = ?, conversion_rate = ?, inr_amount = ?, conversion_amount = ? " +
                    "WHERE disbursement_id = ?";
            logger.debug("Updating disbursement with ID: {}", disbursement.getDisbursementId());
            
            jdbcTemplate.update(sql,
                    disbursement.getCaseId(),
                    disbursement.getClientId(),
                    disbursement.getDisbursement(),
                    disbursement.getDate(),
                    disbursement.getCurrencyCode(),
                    disbursement.getConversionRate(),
                    disbursement.getInrAmount(),
                    disbursement.getConversionAmount(),
                    disbursement.getDisbursementId());
        } catch (DataAccessException e) {
            logger.error("Error updating disbursement: {}", e.getMessage());
            throw new RepositoryException("Failed to update disbursement in database", e);
        }
    }

    public int deleteDisbursementById(String disbursementId) throws RepositoryException {
        try {
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE disbursement_id = ?";
            logger.debug("Deleting disbursement with ID: {}", disbursementId);
            return jdbcTemplate.update(sql, disbursementId);
        } catch (DataAccessException e) {
            logger.error("Error deleting disbursement: {}", e.getMessage());
            throw new RepositoryException("Failed to delete disbursement from database", e);
        }
    }
}
