package com.perfexiolegal.billingsystem.Repository;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Model.DisbursementRequest;
import com.perfexiolegal.billingsystem.Model.DisbursementResponse;
import com.perfexiolegal.billingsystem.Model.DisbursementRequestDto;

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
public class DisbursementsRepository implements IDisbursementsRepository {

    private static final Logger logger = LoggerFactory.getLogger(DisbursementsRepository.class);
    private static final String TABLE_NAME = "ebdb.public.disbursements";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<DisbursementResponse> disbursementsRowMapper = (resultSet, i) -> {
        Long disbursementId = resultSet.getLong("disbursement_id");
        String caseId = resultSet.getString("case_id");
        String clientId = resultSet.getString("client_id");
        String disbursement = resultSet.getString("disbursement");
        String description = resultSet.getString("description");
        Date date = (Date) resultSet.getObject("date");
        String currencyCode = resultSet.getString("currency_code");
        double conversionRate = resultSet.getDouble("conversion_rate");
        double inrAmount = resultSet.getDouble("inr_amount");
        double conversionAmount = resultSet.getDouble("conversion_amount");
        return DisbursementResponse.builder()
                .disbursementId(disbursementId)
                .caseId(caseId)
                .clientId(clientId)
                .disbursement(disbursement)
                .description(description)
                .date(date)
                .currencyCode(currencyCode)
                .conversionRate(conversionRate)
                .inrAmount(inrAmount)
                .conversionAmount(conversionAmount)
                .build();
    };

    @Override
    public Optional<List<DisbursementResponse>> getAll() throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME;
            logger.debug("Retrieving all disbursements");
            List<DisbursementResponse> disbursementList = jdbcTemplate.query(sql, disbursementsRowMapper);
            return Optional.of(disbursementList);
        } catch (DataAccessException e) {
            logger.error("Error retrieving all disbursements: {}", e.getMessage());
            throw new RepositoryException("Failed to retrieve disbursements from database", e);
        }
    }

    @Override
    public Optional<DisbursementResponse> getById(Long id) throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE disbursement_id = ?";
            logger.debug("Retrieving disbursement with ID: {}", id);
            List<DisbursementResponse> disbursements = jdbcTemplate.query(sql, disbursementsRowMapper, id);
            return disbursements.isEmpty() ? Optional.empty() : Optional.of(disbursements.get(0));
        } catch (DataAccessException e) {
            logger.error("Error retrieving disbursement with ID {}: {}", id, e.getMessage());
            throw new RepositoryException("Failed to retrieve disbursement from database", e);
        }
    }

    @Override
    public void create(DisbursementRequest entity) throws RepositoryException {
        try {
            String sql = "INSERT INTO " + TABLE_NAME + 
                    " (case_id, client_id, disbursement, description, date, " +
                    "currency_code, conversion_rate, inr_amount, conversion_amount) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            logger.debug("Creating disbursement for case: {}", entity.getCaseId());
            
            jdbcTemplate.update(sql,
                    entity.getCaseId(),
                    entity.getClientId(),
                    entity.getDisbursement(),
                    entity.getDescription(),
                    entity.getDate(),
                    entity.getCurrencyCode(),
                    entity.getConversionRate(),
                    entity.getInrAmount(),
                    entity.getConversionAmount());
        } catch (DataAccessException e) {
            logger.error("Error creating disbursement: {}", e.getMessage());
            throw new RepositoryException("Failed to create disbursement in database", e);
        }
    }

    @Override
    public void update(Long disbursementId, DisbursementRequestDto entity) throws RepositoryException {
        try {
            String sql = "UPDATE " + TABLE_NAME + 
                    " SET case_id = ?, client_id = ?, disbursement = ?, description = ?, date = ?, " +
                    "currency_code = ?, conversion_rate = ?, inr_amount = ?, conversion_amount = ? " +
                    "WHERE disbursement_id = ?";
            logger.debug("Updating disbursement with ID: {}", disbursementId);
            
            jdbcTemplate.update(sql,
                    entity.getCaseId(),
                    entity.getClientId(),
                    entity.getDisbursement(),
                    entity.getDescription(),
                    entity.getDate(),
                    entity.getCurrencyCode(),
                    entity.getConversionRate(),
                    entity.getInrAmount(),
                    entity.getConversionAmount(),
                    disbursementId);
        } catch (DataAccessException e) {
            logger.error("Error updating disbursement: {}", e.getMessage());
            throw new RepositoryException("Failed to update disbursement in database", e);
        }
    }

    @Override
    public int deleteById(Long id) throws RepositoryException {
        try {
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE disbursement_id = ?";
            logger.debug("Deleting disbursement with ID: {}", id);
            return jdbcTemplate.update(sql, id);
        } catch (DataAccessException e) {
            logger.error("Error deleting disbursement: {}", e.getMessage());
            throw new RepositoryException("Failed to delete disbursement from database", e);
        }
    }

    @Override
    public Optional<List<DisbursementResponse>> getByClientId(String clientID) throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE client_id = ?";
            logger.debug("Retrieving disbursements for client with ID: {}", clientID);
            List<DisbursementResponse> disbursementList = jdbcTemplate.query(sql, disbursementsRowMapper, clientID);
            return Optional.of(disbursementList);
        } catch (DataAccessException e) {
            logger.error("Error retrieving disbursements for client {}: {}", clientID, e.getMessage());
            throw new RepositoryException("Failed to retrieve disbursements from database", e);
        }
    }

    @Override
    public Optional<List<DisbursementResponse>> getByCaseId(String caseID) throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE case_id = ?";
            logger.debug("Retrieving disbursements for case with ID: {}", caseID);
            List<DisbursementResponse> disbursementList = jdbcTemplate.query(sql, disbursementsRowMapper, caseID);
            return Optional.of(disbursementList);
        } catch (DataAccessException e) {
            logger.error("Error retrieving disbursements for case {}: {}", caseID, e.getMessage());
            throw new RepositoryException("Failed to retrieve disbursements from database", e);
        }
    }
}
