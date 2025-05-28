package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.AttorneyDetails;
import com.perfexiolegal.billingsystem.Repository.IAttorneysRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttorneysService implements IAttorneysService {

    private static final Logger logger = LoggerFactory.getLogger(AttorneysService.class);

    @Autowired
    private IAttorneysRepository attorneysRepository;

    public Optional<List<AttorneyDetails>> getAll() throws ServiceException {
        try {
            logger.debug("Retrieving all attorneys");
            return attorneysRepository.getAll();
        } catch (RepositoryException e) {
            logger.error("Failed to retrieve all attorneys", e);
            throw new ServiceException("Unable to retrieve all attorneys", e);
        }
    }

    public Optional<AttorneyDetails> getById(String attorneyID) throws ServiceException {
        try {
            logger.debug("Retrieving attorney with ID: {}", attorneyID);
            return attorneysRepository.getById(attorneyID);
        } catch (RepositoryException e) {
            logger.error("Failed to retrieve attorney with ID: {}", attorneyID, e);
            throw new ServiceException("Unable to retrieve attorney", e);
        }
    }

    public void create(AttorneyDetails attorney) throws ServiceException {
        try {
            logger.debug("Creating new attorney with ID: {}", attorney.getAttorneyId());
            attorneysRepository.create(attorney);
        } catch (RepositoryException e) {
            logger.error("Failed to create attorney with ID: {}", attorney.getAttorneyId(), e);
            throw new ServiceException("Unable to create attorney", e);
        }
    }

    public void update(AttorneyDetails attorney) throws ServiceException {
        try {
            logger.debug("Updating attorney with ID: {}", attorney.getAttorneyId());
            attorneysRepository.update(attorney);
        } catch (RepositoryException e) {
            logger.error("Failed to update attorney with ID: {}", attorney.getAttorneyId(), e);
            throw new ServiceException("Unable to update attorney", e);
        }
    }

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
