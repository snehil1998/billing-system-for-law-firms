package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.Disbursements;
import com.perfexiolegal.billingsystem.Model.Services;
import com.perfexiolegal.billingsystem.Repository.DisbursementsRepository;
import com.perfexiolegal.billingsystem.Repository.ServicesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Service
public class DisbursementsService {

  @Autowired
  DisbursementsRepository disbursementsRepository;

  final Logger logger = LoggerFactory.getLogger(ServicesRepository.class);

  public Optional<List<Disbursements>> getAllDisbursements() throws ServiceException {
    try {
      return disbursementsRepository.getAllDisbursements();
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve all disbursements", e);
    }
  }

  public Optional<Disbursements> getDisbursementsById(UUID disbursementId) throws ServiceException {
    try {
      return disbursementsRepository.getDisbursementsById(disbursementId);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve disbursement for disbursement id", e);
    }
  }

  public Optional<List<Disbursements>> getDisbursementsByClientId(String clientId) throws ServiceException {
    try {
      return disbursementsRepository.getDisbursementsByClientId(clientId);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve all disbursements for client", e);
    }
  }

  public Optional<List<Disbursements>> getDisbursementsByCaseId(String caseId) throws ServiceException {
    try {
      return disbursementsRepository.getDisbursementsByCaseId(caseId);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve all services for case", e);
    }
  }

  public void postDisbursements(Disbursements disbursement) throws ServiceException {
    try {
      disbursementsRepository.postDisbursements(disbursement);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to post disbursement", e);
    }
  }

  public void updateDisbursements(Disbursements disbursement) throws ServiceException {
    try {
      logger.info("updating disbursements through disbursement");
      disbursementsRepository.updateDisbursements(disbursement);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to update disbursement", e);
    }
  }

  public int deleteDisbursementById(UUID disbursementId) throws ServiceException {
    try {
      return disbursementsRepository.deleteDisbursementById(disbursementId);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to delete disbursement", e);
    }
  }
}