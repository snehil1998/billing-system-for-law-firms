package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.ClientDetails;

public interface IClientsService extends IService<ClientDetails> {
    public ClientDetails updateAmounts(String clientID, double disbursementsAmount, double servicesAmount) throws ServiceException;
}
