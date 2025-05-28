package com.perfexiolegal.billingsystem.Repository;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Model.CaseDetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CasesRepository implements ICasesRepository {

    private static final Logger logger = LoggerFactory.getLogger(CasesRepository.class);
    private static final String TABLE_NAME = "ebdb.public.cases";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<CaseDetails> casesRowMapper = (resultSet, i) -> CaseDetails.builder()
            .caseId(resultSet.getString("case_id"))
            .caseName(resultSet.getString("case_name"))
            .clientId(resultSet.getString("client_id"))
            .currencyCode(resultSet.getString("currency_code"))
            .disbursementsAmount(resultSet.getFloat("disbursements_amount"))
            .servicesAmount(resultSet.getFloat("services_amount"))
            .amount(resultSet.getFloat("amount"))
            .build();

    public Optional<List<CaseDetails>> getAll() throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME;
            logger.debug("Retrieving all cases");
            List<CaseDetails> casesList = jdbcTemplate.query(sql, casesRowMapper);
            return Optional.of(casesList);
        } catch (DataAccessException e) {
            logger.error("Error retrieving all cases: {}", e.getMessage());
            throw new RepositoryException("Failed to retrieve cases from database", e);
        }
    }

    public Optional<CaseDetails> getById(String caseID) throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE case_id = ?";
            logger.debug("Retrieving case with ID: {}", caseID);
            List<CaseDetails> cases = jdbcTemplate.query(sql, casesRowMapper, caseID);
            return cases.isEmpty() ? Optional.empty() : Optional.of(cases.get(0));
        } catch (DataAccessException e) {
            logger.error("Error retrieving case with ID {}: {}", caseID, e.getMessage());
            throw new RepositoryException("Failed to retrieve case from database", e);
        }
    }

    public void create(CaseDetails postCase) throws RepositoryException {
        try {
            String sql = "INSERT INTO " + TABLE_NAME + 
                    " (case_id, case_name, client_id, currency_code, disbursements_amount, services_amount, amount) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            logger.debug("Creating case with ID: {}", postCase.getCaseId());
            jdbcTemplate.update(sql,
                    postCase.getCaseId(),
                    postCase.getCaseName(),
                    postCase.getClientId(),
                    postCase.getCurrencyCode(),
                    postCase.getDisbursementsAmount(),
                    postCase.getServicesAmount(),
                    postCase.getAmount());
        } catch (DataAccessException e) {
            logger.error("Error creating case: {}", e.getMessage());
            throw new RepositoryException("Failed to create case in database", e);
        }
    }

    public void update(CaseDetails updatedCase) throws RepositoryException {
        try {
            String sql = "UPDATE " + TABLE_NAME + 
                    " SET case_name = ?, client_id = ?, currency_code = ?, " +
                    "disbursements_amount = ?, services_amount = ?, amount = ? " +
                    "WHERE case_id = ?";
            logger.debug("Updating case with ID: {}", updatedCase.getCaseId());
            jdbcTemplate.update(sql,
                    updatedCase.getCaseName(),
                    updatedCase.getClientId(),
                    updatedCase.getCurrencyCode(),
                    updatedCase.getDisbursementsAmount(),
                    updatedCase.getServicesAmount(),
                    updatedCase.getAmount(),
                    updatedCase.getCaseId());
        } catch (DataAccessException e) {
            logger.error("Error updating case: {}", e.getMessage());
            throw new RepositoryException("Failed to update case in database", e);
        }
    }

    public int deleteById(String caseID) throws RepositoryException {
        try {
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE case_id = ?";
            logger.debug("Deleting case with ID: {}", caseID);
            return jdbcTemplate.update(sql, caseID);
        } catch (DataAccessException e) {
            logger.error("Error deleting case: {}", e.getMessage());
            throw new RepositoryException("Failed to delete case from database", e);
        }
    }
}
