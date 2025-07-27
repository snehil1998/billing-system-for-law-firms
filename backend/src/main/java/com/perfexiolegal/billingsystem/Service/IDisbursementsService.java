package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.DisbursementRequest;
import com.perfexiolegal.billingsystem.Model.DisbursementResponse;

import java.util.List;
import java.util.Optional;

public interface IDisbursementsService {
    Optional<List<DisbursementResponse>> getByClientId(String clientId) throws ServiceException;
    Optional<List<DisbursementResponse>> getByCaseId(String caseId) throws ServiceException;
    Optional<List<DisbursementResponse>> getAll() throws ServiceException;
    Optional<DisbursementResponse> getById(Long id) throws ServiceException;
    void create(DisbursementRequest entity) throws ServiceException;
    void update(Long id, DisbursementRequest entity) throws ServiceException;
    int deleteById(Long id) throws ServiceException;
}