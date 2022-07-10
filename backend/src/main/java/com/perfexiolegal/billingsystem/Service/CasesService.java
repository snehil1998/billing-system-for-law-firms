package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.Cases;
import com.perfexiolegal.billingsystem.Repository.CasesRepository;
import com.perfexiolegal.billingsystem.Repository.ServicesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Service
public class CasesService {

  @Autowired
  CasesRepository casesRepository;

  final Logger logger = LoggerFactory.getLogger(ServicesRepository.class);

  public Optional<List<Cases>> getAllCases() throws ServiceException {
    try {
      return casesRepository.getAllCases();
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve all cases", e);
    }
  }

  public Optional<Cases> getCaseById(UUID caseID) throws ServiceException {
    try {
      return casesRepository.getCaseById(caseID);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve all services", e);
    }
  }

  public Cases postCases(Cases postCase) throws ServiceException {
    try {
      return casesRepository.postCases(postCase);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to post service", e);
    }
  }

  public Cases updateCases(Cases updatedCase) throws ServiceException {
    try {
      return casesRepository.updateCases(updatedCase);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to update service", e);
    }
  }

  public int deleteById(UUID caseID) throws ServiceException {
    try {
      return casesRepository.deleteById(caseID);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to delete service", e);
    }
  }

}
