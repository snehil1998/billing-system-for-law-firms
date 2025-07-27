package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.CaseDetails;
import com.perfexiolegal.billingsystem.Model.ServiceRequestDto;
import com.perfexiolegal.billingsystem.Model.ServiceRequest;
import com.perfexiolegal.billingsystem.Model.ServiceResponse;
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

  public Optional<List<ServiceResponse>> getAll() throws ServiceException {
    try {
      return servicesRepository.getAll();
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve all services", e);
    }
  }

  public Optional<List<ServiceResponse>> getByCaseId(String caseID) throws ServiceException {
    try {
      return servicesRepository.getByCaseId(caseID);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve all services for case", e);
    }
  }

  public Optional<List<ServiceResponse>> getByClientId(String clientID) throws ServiceException {
    try {
      return servicesRepository.getByClientId(clientID);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve all services for client", e);
    }
  }

  public Optional<ServiceResponse> getById(Long serviceID) throws ServiceException {
    try {
      return servicesRepository.getById(serviceID);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve service", e);
    }
  }

  public void create(ServiceRequest request) throws ServiceException {
    try {
      ServiceRequestDto service = servicesTransformer.ToServiceRequestDto(request);
      servicesRepository.create(service);
      casesService.updateAmounts(service.getCaseId(), 0, service.getAmount());
      clientsService.updateAmounts(service.getClientId(), 0, service.getAmount());
    } catch (RepositoryException e) {
      throw new ServiceException("unable to post service", e);
    }
  }

  public void update(Long serviceId, ServiceRequest request) throws ServiceException {
    try {
      logger.info("updating services through service");
      ServiceResponse service = getServiceIfExists(serviceId);
      ServiceRequestDto serviceDto = servicesTransformer.ToServiceRequestDto(request);
      servicesRepository.update(serviceId, serviceDto);
      double updatedAmount = serviceDto.getAmount() - service.getAmount();
      casesService.updateAmounts(service.getCaseId(), 0, updatedAmount);
      clientsService.updateAmounts(service.getClientId(), 0, updatedAmount);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to update service", e);
    }
  }

  public int deleteById(Long serviceID) throws ServiceException {
    try {
      ServiceResponse service = getServiceIfExists(serviceID);
      int result = servicesRepository.deleteById(serviceID);
      casesService.updateAmounts(service.getCaseId(), 0, -service.getAmount());
      clientsService.updateAmounts(service.getClientId(), 0, -service.getAmount());
      return result;
    } catch (RepositoryException e) {
      throw new ServiceException("unable to delete service", e);
    }
  }

  public int deleteByCaseId(String caseID) throws ServiceException {
    try {
      Optional<CaseDetails> caseDetails = casesService.getById(caseID);
      if (caseDetails.isEmpty()) {
        throw new ServiceException("Case not found with ID: " + caseID);
      }
      int result = servicesRepository.deleteByCaseId(caseID);
      casesService.updateAmounts(caseID, 0, -caseDetails.get().getAmount());
      return result;
    } catch (RepositoryException e) {
      throw new ServiceException("unable to delete all services for caseID: " + caseID, e);
    }
  }

  private ServiceResponse getServiceIfExists(Long serviceID) throws ServiceException {
    Optional<ServiceResponse> service = getById(serviceID);
    if (service.isEmpty()) {
        throw new ServiceException("Service not found with ID: " + serviceID);
    }
    return service.get();
  }
}
