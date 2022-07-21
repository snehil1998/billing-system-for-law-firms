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

import java.util.UUID;

@Component
public class ServicesTransformer {

  @Autowired
  AttorneysService attorneysService;

  final Logger logger = LoggerFactory.getLogger(ServicesTransformer.class);

  public Services fromJsonWithoutIDAndAmount(ServicesWithoutIdAndAmount service)
      throws ServiceException {
    double[] amount = {0};
    service.getAttorneys().stream().forEach(attorneys -> {
      Attorneys attorney = null;
      try {
        attorney = attorneysService.getAttorneyById(attorneys.getId()).get();
      } catch (ServiceException e) {
        logger.info("Cannot retrieve attorney with id: {}", attorneys.getId());
      }

      if (attorney != null) {
        ServicePricing filteredServicePricing = attorney.getServicePricing().stream()
            .filter(servicePricing -> servicePricing.getClientId().equals(service.getClientId()))
            .findAny().orElse(null);
        if (filteredServicePricing != null) {
          amount[0] += (filteredServicePricing.getPrice() * attorneys.getMinutes());
        } else {
          logger.info("Cannot find service pricing for the attorney id: {} and client id: {}",
              attorney.getAttorneyId(), service.getClientId());
        }
      } else {
        logger.info("No attorney with id: {}", attorneys.getId());
      }

    });

    return new Services(
        UUID.randomUUID(),
        service.getCaseId(),
        service.getClientId(),
        service.getService(),
        service.getDescription(),
        service.getDate(),
        service.getAttorneys(),
        amount[0]
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
