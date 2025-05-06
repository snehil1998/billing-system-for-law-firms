package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.Attorneys;
import com.perfexiolegal.billingsystem.Repository.AttorneysRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing attorney-related business logic.
 * Handles operations such as retrieving, creating, updating, and deleting attorneys.
 */
@Service
public class AttorneysService {

    private static final Logger logger = LoggerFactory.getLogger(AttorneysService.class);
    private final AttorneysRepository attorneysRepository;

    @Autowired
    public AttorneysService(AttorneysRepository attorneysRepository) {
        this.attorneysRepository = attorneysRepository;
    }

    /**
     * Retrieves all attorneys from the system.
     * @return Optional containing a list of attorneys, or empty if none found
     * @throws ServiceException if there's an error retrieving the attorneys
     */
    public Optional<List<Attorneys>> getAllAttorneys() throws ServiceException {
        try {
            logger.debug("Retrieving all attorneys");
            return attorneysRepository.getAllAttorneys();
        } catch (RepositoryException e) {
            logger.error("Failed to retrieve all attorneys", e);
            throw new ServiceException("Unable to retrieve all attorneys", e);
        }
    }

    /**
     * Retrieves a specific attorney by their ID.
     * @param attorneyID The ID of the attorney to retrieve
     * @return Optional containing the attorney if found
     * @throws ServiceException if there's an error retrieving the attorney
     */
    public Optional<Attorneys> getAttorneyById(String attorneyID) throws ServiceException {
        try {
            logger.debug("Retrieving attorney with ID: {}", attorneyID);
            return attorneysRepository.getAttorneyById(attorneyID);
        } catch (RepositoryException e) {
            logger.error("Failed to retrieve attorney with ID: {}", attorneyID, e);
            throw new ServiceException("Unable to retrieve attorney", e);
        }
    }

    /**
     * Creates a new attorney in the system.
     * @param attorney The attorney data to create
     * @return The created attorney
     * @throws ServiceException if there's an error creating the attorney
     */
    public Attorneys postAttorneys(Attorneys attorney) throws ServiceException {
        try {
            logger.debug("Creating new attorney with ID: {}", attorney.getAttorneyId());
            return attorneysRepository.postAttorneys(attorney);
        } catch (RepositoryException e) {
            logger.error("Failed to create attorney with ID: {}", attorney.getAttorneyId(), e);
            throw new ServiceException("Unable to create attorney", e);
        }
    }

    /**
     * Updates an existing attorney's information.
     * @param attorney The updated attorney data
     * @return The updated attorney
     * @throws ServiceException if there's an error updating the attorney
     */
    public Attorneys updateAttorney(Attorneys attorney) throws ServiceException {
        try {
            logger.debug("Updating attorney with ID: {}", attorney.getAttorneyId());
            return attorneysRepository.updateAttorneys(attorney);
        } catch (RepositoryException e) {
            logger.error("Failed to update attorney with ID: {}", attorney.getAttorneyId(), e);
            throw new ServiceException("Unable to update attorney", e);
        }
    }

    /**
     * Deletes an attorney by their ID.
     * @param attorneyID The ID of the attorney to delete
     * @return The number of records deleted (1 if successful, 0 if not found)
     * @throws ServiceException if there's an error deleting the attorney
     */
    public int deleteById(String attorneyID) throws ServiceException {
        try {
            logger.debug("Deleting attorney with ID: {}", attorneyID);
            return attorneysRepository.deleteById(attorneyID);
        } catch (RepositoryException e) {
            logger.error("Failed to delete attorney with ID: {}", attorneyID, e);
            throw new ServiceException("Unable to delete attorney", e);
        }
    }
}
