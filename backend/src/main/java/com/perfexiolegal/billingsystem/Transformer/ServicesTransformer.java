package com.perfexiolegal.billingsystem.Transformer;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.Clients;
import com.perfexiolegal.billingsystem.Model.Services;
import com.perfexiolegal.billingsystem.Model.ServicesWithoutId;
import com.perfexiolegal.billingsystem.Model.ServicesWithoutIdAndAmount;
import com.perfexiolegal.billingsystem.Service.ClientsService;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.UUID;

@Component
public class ServicesTransformer {

  @Autowired
  ClientsService clientsService;

  final Logger logger = LoggerFactory.getLogger(ServicesTransformer.class);

  public Services fromJsonWithoutIDAndAmount(ServicesWithoutIdAndAmount service) throws ServiceException {
    Clients client = clientsService.getClientById(service.getClientId()).get();
//    logger.info("Updated amount for minutes: " + Math.ceil(service.getMinutes() / 60.0) * client.getServicePricing());
    float amount = 0;
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
