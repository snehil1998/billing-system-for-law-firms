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

@Component
public class AttorneysTransformer {

  @Autowired
  AttorneysService attorneysService;

  public Attorneys update(AttorneysWithoutId attorney, UUID attorneyID) throws ServiceException {
    List<ServicePricing> servicePricingList = attorneysService.getAttorneyById(attorneyID).get()
        .getServicePricing();
    attorney.getServicePricing().stream().map(servicePricingList::add);
    return new Attorneys(
        attorneyID,
        attorney.getFirstName(),
        attorney.getLastName(),
        servicePricingList
    );
  }
}
