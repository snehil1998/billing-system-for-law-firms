package com.perfexiolegal.billingsystem.Transformer;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.AttorneyDetails;
import com.perfexiolegal.billingsystem.Model.AttorneyMinutes;
import com.perfexiolegal.billingsystem.Model.ServiceRequest;
import com.perfexiolegal.billingsystem.Model.ServiceRequestDto;
import com.perfexiolegal.billingsystem.Model.ServicePricing;
import com.perfexiolegal.billingsystem.Service.AttorneysService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServicesTransformer {

  @Autowired
  private AttorneysService attorneysService;

  final Logger logger = LoggerFactory.getLogger(ServicesTransformer.class);

  public double computeAmount(List<AttorneyMinutes> attorneys, String clientId)
      throws ServiceException {
    double[] amount = {0};
    attorneys.stream().forEach(attorney -> {
      AttorneyDetails attorneyDetails = null;
      try {
        attorneyDetails = attorneysService.getById(attorney.getId()).get();
      } catch (ServiceException e) {
        logger.info("Failed to retrieve attorney with id: {}", attorney.getId());
      }

      if (attorneyDetails != null) {
        ServicePricing filteredServicePricing = attorneyDetails.getServicePricing().stream()
            .filter(servicePricing -> servicePricing.getClientId().equals(clientId))
            .findAny().orElse(null);
        if (filteredServicePricing != null) {
          amount[0] += (filteredServicePricing.getPrice() * (attorney.getMinutes()/60.0));
        } else {
          logger.info("Cannot find service pricing for the attorney id: {} and client id: {}",
              attorneyDetails.getAttorneyId(), clientId);
        }
      } else {
        logger.info("No attorney with id: {}", attorney.getId());
      }

    });

    return amount[0];
  }

  public ServiceRequestDto ToServiceRequestDto(ServiceRequest request) throws ServiceException {
    double amount = computeAmount(request.getAttorneys(), request.getClientId());
    return ServiceRequestDto.builder()
      .caseId(request.getCaseId())
      .clientId(request.getClientId())
      .service(request.getService())
      .description(request.getDescription())
      .date(request.getDate())
      .attorneys(request.getAttorneys())
      .amount(amount)
      .build();
  }
}
