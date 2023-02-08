package com.perfexiolegal.billingsystem.Transformer;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.Attorneys;
import com.perfexiolegal.billingsystem.Model.AttorneysWithoutId;
import com.perfexiolegal.billingsystem.Model.Clients;
import com.perfexiolegal.billingsystem.Model.ClientsWithoutId;
import com.perfexiolegal.billingsystem.Model.ServicePricing;
import com.perfexiolegal.billingsystem.Service.AttorneysService;
import com.perfexiolegal.billingsystem.Service.ClientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class AttorneysTransformer {

  @Autowired
  AttorneysService attorneysService;

  public Attorneys update(AttorneysWithoutId attorney, String attorneyID) throws ServiceException {
    List<ServicePricing> servicePricingList = attorneysService.getAttorneyById(attorneyID).get()
        .getServicePricing();
    servicePricingList.addAll(attorney.getServicePricing());
    return new Attorneys(
        attorneyID,
        attorney.getFirstName(),
        attorney.getLastName(),
        servicePricingList
    );
  }

  public Attorneys deleteServicePrice(Attorneys attorney, String clientID) throws ServiceException {
    List<ServicePricing> servicePricingList = attorney.getServicePricing();
    List<ServicePricing> filteredServicePricingList = servicePricingList.stream()
        .filter(pricing -> !pricing.getClientId().equals(clientID))
        .collect(Collectors.toList());
    return new Attorneys(
        attorney.getAttorneyId(),
        attorney.getFirstName(),
        attorney.getLastName(),
        filteredServicePricingList
    );
  }

  public Attorneys updateWithUUID(AttorneysWithoutId attorney, String attorneyID) {
    return new Attorneys(
        attorneyID,
        attorney.getFirstName(),
        attorney.getLastName(),
        attorney.getServicePricing()
    );
  }
}
