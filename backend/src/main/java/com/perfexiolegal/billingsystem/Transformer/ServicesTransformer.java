package com.perfexiolegal.billingsystem.Transformer;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.Attorneys;
import com.perfexiolegal.billingsystem.Model.ServicePricing;
import com.perfexiolegal.billingsystem.Model.Services;
import com.perfexiolegal.billingsystem.Model.ServicesWithoutId;
import com.perfexiolegal.billingsystem.Model.ServicesWithoutIdAndAmount;
import com.perfexiolegal.billingsystem.Service.AttorneysService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ServicesTransformer {

  @Autowired
  AttorneysService attorneysService;

  final Logger logger = LoggerFactory.getLogger(ServicesTransformer.class);

  public Services fromJsonWithoutIDAndAmount(ServicesWithoutIdAndAmount service)
      throws ServiceException {
    Attorneys attorneys = attorneysService.getAttorneyById(UUID.fromString("bc37c7ca-0175-4fdb-8b3e-a1952a271a98")).get();
//    logger.info("Updated amount for minutes: " + Math.ceil(service.getMinutes() / 60.0) * client.getServicePricing());
    float amount = 0;
    List<ServicePricing> map = attorneys.getServicePricing();
//    for(Object attorney : service.getAttorneyIds()) {
//      amount += (int)(((JSONObject) attorney).get("minutes")) / 60.0 * client.getServicePricing();
//
//    }
    return new Services(
        UUID.randomUUID(),
        service.getCaseId(),
        service.getClientId(),
        service.getService(),
        service.getDescription(),
        service.getDate(),
        service.getAttorneys(),
        amount
    );
  }

  public Services update(ServicesWithoutId service, UUID serviceID) {
    return new Services(
        serviceID,
        service.getCaseId(),
        service.getClientId(),
        service.getService(),
        service.getDescription(),
        service.getDate(),
        service.getAttorneys(),
        service.getAmount()
    );
  }

}
