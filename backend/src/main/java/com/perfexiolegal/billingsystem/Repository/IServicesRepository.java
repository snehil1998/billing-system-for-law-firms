package com.perfexiolegal.billingsystem.Repository;

import java.util.List;
import java.util.Optional;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Model.ServiceDetails;


public interface IServicesRepository extends IRepository<ServiceDetails>{
    Optional<List<ServiceDetails>> getByCaseId(String caseID) throws RepositoryException;
    int deleteByCaseId(String caseID) throws RepositoryException;
    Optional<List<ServiceDetails>> getByClientId(String clientID) throws RepositoryException;
}
