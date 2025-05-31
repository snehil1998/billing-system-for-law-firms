package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.ServiceDetails;
import com.perfexiolegal.billingsystem.Repository.IServicesRepository;
import com.perfexiolegal.billingsystem.Repository.ServicesRepository;
import com.perfexiolegal.billingsystem.Transformer.ServicesTransformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServicesService implements IServicesService {

  @Autowired
  private IServicesRepository servicesRepository;

  @Autowired
  private ICasesService casesService;

  @Autowired
  private IClientsService clientsService;

  @Autowired
  private ServicesTransformer servicesTransformer;

  private static final Logger logger = LoggerFactory.getLogger(ServicesRepository.class);

  public Optional<List<ServiceDetails>> getAll() throws ServiceException {
    try {
      return servicesRepository.getAll();
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve all services", e);
    }
  }

  public Optional<List<ServiceDetails>> getByCaseId(String caseID) throws ServiceException {
    try {
      return servicesRepository.getByCaseId(caseID);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve all services for case", e);
    }
  }

  public Optional<List<ServiceDetails>> getByClientId(String clientID) throws ServiceException {
    try {
      return servicesRepository.getByClientId(clientID);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve all services for client", e);
    }
  }

  public Optional<ServiceDetails> getById(String serviceID) throws ServiceException {
    try {
      return servicesRepository.getById(serviceID);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve service", e);
    }
  }

  public void create(ServiceDetails serviceDetails) throws ServiceException {
    try {
      ServiceDetails service = servicesTransformer.populateAmount(serviceDetails);
      casesService.updateAmounts(service.getCaseId(), 0, service.getAmount());
      clientsService.updateAmounts(service.getClientId(), 0, service.getAmount());
      servicesRepository.create(service);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to post service", e);
    }
  }

  public void update(ServiceDetails serviceDetails) throws ServiceException {
    try {
      logger.info("updating services through service");
      validateServiceExists(serviceDetails.getServiceId());
      ServiceDetails service = servicesTransformer.populateAmount(serviceDetails);
      servicesRepository.update(service);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to update service", e);
    }
  }

  public int deleteById(String serviceID) throws ServiceException {
    try {
      ServiceDetails service = getById(serviceID).get();
      casesService.updateAmounts(service.getCaseId(), 0, -service.getAmount());
      clientsService.updateAmounts(service.getClientId(), 0, -service.getAmount());
      return servicesRepository.deleteById(serviceID);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to delete service", e);
    }
  }

  public int deleteByCaseId(String caseID) throws ServiceException {
    try {
      return servicesRepository.deleteByCaseId(caseID);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to delete all services", e);
    }
  }

  private void validateServiceExists(String serviceID) throws ServiceException {
    Optional<ServiceDetails> service = getById(serviceID);
    if (service.isEmpty()) {
        throw new ServiceException("Service not found with ID: " + serviceID);
    }
  }
}
