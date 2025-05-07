package com.perfexiolegal.billingsystem.Repository;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Model.Disbursements;
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

/**
 * Repository class for handling disbursement-related database operations.
 * Provides methods for CRUD operations on disbursements.
 */
@Repository
public class DisbursementsRepository {

    private static final Logger logger = LoggerFactory.getLogger(DisbursementsRepository.class);
    private static final String TABLE_NAME = "ebdb.public.disbursements";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Disbursements> disbursementsRowMapper = (resultSet, i) -> {
        String disbursementId = resultSet.getString("disbursement_id");
        String caseId = resultSet.getString("case_id");
        String clientId = resultSet.getString("client_id");
        String disbursement = resultSet.getString("disbursement");
        Date date = (Date) resultSet.getObject("date");
        String currencyCode = resultSet.getString("currency_code");
        double conversionRate = resultSet.getDouble("conversion_rate");
        double inrAmount = resultSet.getDouble("inr_amount");
        double conversionAmount = resultSet.getDouble("conversion_amount");
        return Disbursements.builder()
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

    /**
     * Retrieves all disbursements from the database.
     * @return Optional containing a list of all disbursements, or empty if none found
     * @throws RepositoryException if there is an error accessing the database
     */
    public Optional<List<Disbursements>> getAllDisbursements() throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME;
            logger.debug("Retrieving all disbursements");
            List<Disbursements> disbursementList = jdbcTemplate.query(sql, disbursementsRowMapper);
            return Optional.of(disbursementList);
        } catch (DataAccessException e) {
            logger.error("Error retrieving all disbursements: {}", e.getMessage());
            throw new RepositoryException("Failed to retrieve disbursements from database", e);
        }
    }

    /**
     * Retrieves a specific disbursement by ID.
     * @param disbursementId The ID of the disbursement to retrieve
     * @return Optional containing the disbursement if found, empty otherwise
     * @throws RepositoryException if there is an error accessing the database
     */
    public Optional<Disbursements> getDisbursementsById(String disbursementId) throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE disbursement_id = ?";
            logger.debug("Retrieving disbursement with ID: {}", disbursementId);
            List<Disbursements> disbursements = jdbcTemplate.query(sql, disbursementsRowMapper, disbursementId);
            return disbursements.isEmpty() ? Optional.empty() : Optional.of(disbursements.get(0));
        } catch (DataAccessException e) {
            logger.error("Error retrieving disbursement with ID {}: {}", disbursementId, e.getMessage());
            throw new RepositoryException("Failed to retrieve disbursement from database", e);
        }
    }

    /**
     * Retrieves all disbursements for a specific client.
     * @param clientID The ID of the client
     * @return Optional containing a list of disbursements for the client, or empty if none found
     * @throws RepositoryException if there is an error accessing the database
     */
    public Optional<List<Disbursements>> getDisbursementsByClientId(String clientID) throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE client_id = ?";
            logger.debug("Retrieving disbursements for client with ID: {}", clientID);
            List<Disbursements> disbursementList = jdbcTemplate.query(sql, disbursementsRowMapper, clientID);
            return Optional.of(disbursementList);
        } catch (DataAccessException e) {
            logger.error("Error retrieving disbursements for client {}: {}", clientID, e.getMessage());
            throw new RepositoryException("Failed to retrieve disbursements from database", e);
        }
    }

    /**
     * Retrieves all disbursements for a specific case.
     * @param caseID The ID of the case
     * @return Optional containing a list of disbursements for the case, or empty if none found
     * @throws RepositoryException if there is an error accessing the database
     */
    public Optional<List<Disbursements>> getDisbursementsByCaseId(String caseID) throws RepositoryException {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE case_id = ?";
            logger.debug("Retrieving disbursements for case with ID: {}", caseID);
            List<Disbursements> disbursementList = jdbcTemplate.query(sql, disbursementsRowMapper, caseID);
            return Optional.of(disbursementList);
        } catch (DataAccessException e) {
            logger.error("Error retrieving disbursements for case {}: {}", caseID, e.getMessage());
            throw new RepositoryException("Failed to retrieve disbursements from database", e);
        }
    }

    /**
     * Creates a new disbursement in the database.
     * @param disbursement The disbursement to create
     * @throws RepositoryException if there is an error accessing the database
     */
    public void postDisbursements(Disbursements disbursement) throws RepositoryException {
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

    /**
     * Updates an existing disbursement in the database.
     * @param disbursement The disbursement data to update
     * @throws RepositoryException if there is an error accessing the database
     */
    public void updateDisbursements(Disbursements disbursement) throws RepositoryException {
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

    /**
     * Deletes a disbursement from the database.
     * @param disbursementId The ID of the disbursement to delete
     * @return The number of rows affected (1 if successful, 0 if not found)
     * @throws RepositoryException if there is an error accessing the database
     */
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
