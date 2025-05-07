package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.ServiceDetails;
import com.perfexiolegal.billingsystem.Model.Services;
import com.perfexiolegal.billingsystem.Repository.ServicesRepository;
import com.perfexiolegal.billingsystem.Transformer.ServicesTransformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServicesService {

  @Autowired
  ServicesRepository servicesRepository;

  @Autowired
  CasesService casesService;

  @Autowired
  ClientsService clientsService;

  @Autowired
  ServicesTransformer servicesTransformer;

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

  public Optional<Services> getServiceFromId(String serviceID) throws ServiceException {
    try {
      return servicesRepository.getServiceFromId(serviceID);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve service", e);
    }
  }

  public Services postServices(ServiceDetails serviceDetails) throws ServiceException {
    try {
      Services service = servicesTransformer.populateAmount(serviceDetails);
      casesService.updateAmounts(service.getCaseId(), 0, service.getAmount());
      clientsService.updateAmounts(service.getClientId(), 0, service.getAmount());
      return servicesRepository.postServices(service);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to post service", e);
    }
  }

  public Services updateServices(ServiceDetails serviceDetails) throws ServiceException {
    try {
      logger.info("updating services through service");
      validateServiceExists(serviceDetails.getServiceId());
      Services service = servicesTransformer.populateAmount(serviceDetails);
      return servicesRepository.updateServices(service);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to update service", e);
    }
  }

  public int deleteById(String serviceID) throws ServiceException {
    try {
      Services service = getServiceFromId(serviceID).get();
      casesService.updateAmounts(service.getCaseId(), 0, -service.getAmount());
      clientsService.updateAmounts(service.getClientId(), 0, -service.getAmount());
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

  private void validateServiceExists(String serviceID) throws ServiceException {
    Optional<Services> service = getServiceFromId(serviceID);
    if (service.isEmpty()) {
        throw new ServiceException("Service not found with ID: " + serviceID);
    }
  }
}
