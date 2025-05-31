package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.ServiceDetails;

import java.util.List;
import java.util.Optional;

public interface IServicesService extends IService<ServiceDetails> {
    Optional<List<ServiceDetails>> getByClientId(String clientId) throws ServiceException;
    int deleteByCaseId(String caseID) throws ServiceException;
    Optional<List<ServiceDetails>> getByCaseId(String caseId) throws ServiceException;
}