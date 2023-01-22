package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.Cases;
import com.perfexiolegal.billingsystem.Model.Clients;
import com.perfexiolegal.billingsystem.Model.Services;
import com.perfexiolegal.billingsystem.Repository.ServicesRepository;
import com.perfexiolegal.billingsystem.Transformer.CasesTransformer;
import com.perfexiolegal.billingsystem.Transformer.ClientsTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Service
public class ServicesService {

  @Autowired
  ServicesRepository servicesRepository;

  @Autowired
  CasesService casesService;

  @Autowired
  CasesTransformer casesTransformer;

  @Autowired
  ClientsService clientsService;

  @Autowired
  ClientsTransformer clientsTransformer;

  final Logger logger = LoggerFactory.getLogger(ServicesRepository.class);

  public Optional<List<Services>> getAllServices() throws ServiceException {
    try {
      return servicesRepository.getAllServices();
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve all services", e);
    }
  }

  public Optional<List<Services>> getServicesForCase(String caseID) throws ServiceException {
    try {
      return servicesRepository.getServicesForCase(caseID);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve all services for case", e);
    }
  }

  public Optional<List<Services>> getServicesForClient(String clientID) throws ServiceException {
    try {
      return servicesRepository.getServicesForClient(clientID);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve all services for client", e);
    }
  }

  public Optional<Services> getServiceFromId(UUID serviceID) throws ServiceException {
    try {
      return servicesRepository.getServiceFromId(serviceID);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve service", e);
    }
  }

  public Services postServices(Services service) throws ServiceException {
    try {
      Cases caseForService = casesService.getCaseById(service.getCaseId()).get();
      Cases updatedCase = casesTransformer.updateAmount(caseForService, 0, service.getAmount());
      casesService.updateCases(updatedCase);

      Clients clientForService = clientsService.getClientById(service.getClientId()).get();
      Clients updatedClient = clientsTransformer.updateAmount(clientForService, 0, service.getAmount());
      clientsService.updateClient(updatedClient);

      return servicesRepository.postServices(service);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to post service", e);
    }
  }

  public Services updateServices(Services service) throws ServiceException {
    try {
      logger.info("updating services through service");
      return servicesRepository.updateServices(service);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to update service", e);
    }
  }

  public int deleteById(UUID serviceID) throws ServiceException {
    try {
      Services service = getServiceFromId(serviceID).get();

      Cases caseForService = casesService.getCaseById(service.getCaseId()).get();
      Cases updatedCase = casesTransformer.updateAmount(caseForService, 0, -service.getAmount());
      casesService.updateCases(updatedCase);

      Clients clientForService = clientsService.getClientById(service.getClientId()).get();
      Clients updatedClient = clientsTransformer.updateAmount(clientForService, 0, -service.getAmount());
      clientsService.updateClient(updatedClient);
      
      return servicesRepository.deleteById(serviceID);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to delete service", e);
    }
  }

  public int deleteByCase(String caseID) throws ServiceException {
    try {
      return servicesRepository.deleteByCase(caseID);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to delete all services", e);
    }
  }
}
