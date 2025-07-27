package com.perfexiolegal.billingsystem.Repository;

import java.util.List;
import java.util.Optional;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Model.ServiceRequestDto;
import com.perfexiolegal.billingsystem.Model.ServiceResponse;


public interface IServicesRepository{
    Optional<List<ServiceResponse>> getByCaseId(String caseID) throws RepositoryException;
    int deleteByCaseId(String caseID) throws RepositoryException;
    Optional<List<ServiceResponse>> getByClientId(String clientID) throws RepositoryException;
    Optional<ServiceResponse> getById(Long id) throws RepositoryException;
    int deleteById(Long id) throws RepositoryException;
    Optional<List<ServiceResponse>> getAll() throws RepositoryException;
    void create(ServiceRequestDto entity) throws RepositoryException;
    void update(Long serviceId, ServiceRequestDto entity) throws RepositoryException;
}
