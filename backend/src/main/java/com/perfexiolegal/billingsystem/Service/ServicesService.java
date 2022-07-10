package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.Services;
import com.perfexiolegal.billingsystem.Repository.ServicesRepository;
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

  final Logger logger = LoggerFactory.getLogger(ServicesRepository.class);

  public Optional<List<Services>> getAllServices() throws ServiceException {
    try {
      return servicesRepository.getAllServices();
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve all services", e);
    }
  }

  public Optional<List<Services>> getServicesForCase(UUID caseID) throws ServiceException {
    try {
      return servicesRepository.getServicesForCase(caseID);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve all services", e);
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
      return servicesRepository.deleteById(serviceID);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to delete service", e);
    }
  }

  public int deleteByCase(UUID caseID) throws ServiceException {
    try {
      return servicesRepository.deleteByCase(caseID);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to delete all services", e);
    }
  }
}
