package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.ServiceRequest;
import com.perfexiolegal.billingsystem.Model.ServiceResponse;

import java.util.List;
import java.util.Optional;

public interface IServicesService {
    Optional<List<ServiceResponse>> getByClientId(String clientId) throws ServiceException;
    int deleteByCaseId(String caseID) throws ServiceException;
    Optional<List<ServiceResponse>> getByCaseId(String caseId) throws ServiceException;
    Optional<List<ServiceResponse>> getAll() throws ServiceException;
    Optional<ServiceResponse> getById(Long id) throws ServiceException;
    void create(ServiceRequest entity) throws ServiceException;
    void update(Long serviceId, ServiceRequest entity) throws ServiceException;
    int deleteById(Long id) throws ServiceException;
}