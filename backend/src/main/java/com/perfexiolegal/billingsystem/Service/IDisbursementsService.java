package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.DisbursementDetails;

import java.util.List;
import java.util.Optional;

public interface IDisbursementsService extends IService<DisbursementDetails> {
    Optional<List<DisbursementDetails>> getByClientId(String clientId) throws ServiceException;
    Optional<List<DisbursementDetails>> getByCaseId(String caseId) throws ServiceException;
}