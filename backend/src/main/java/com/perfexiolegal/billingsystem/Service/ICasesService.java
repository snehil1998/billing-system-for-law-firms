package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.CaseDetails;

public interface ICasesService extends IService<CaseDetails> {
    public CaseDetails updateAmounts(String caseID, double disbursementsAmount, double servicesAmount) throws ServiceException;
}
