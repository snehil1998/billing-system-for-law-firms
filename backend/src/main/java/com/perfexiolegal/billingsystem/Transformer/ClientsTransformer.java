package com.perfexiolegal.billingsystem.Transformer;

import com.perfexiolegal.billingsystem.Model.ClientDetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ClientsTransformer {

  private static final Logger logger = LoggerFactory.getLogger(ClientsTransformer.class);

  public ClientDetails updateAmount(ClientDetails updatedClient, double disbursementsAmount, double servicesAmount) {
    logger.debug("Updating amounts for client with ID: {}", updatedClient.getClientId());
    return new ClientDetails(
        updatedClient.getClientId(),
        updatedClient.getClientName(),
        updatedClient.getCurrencyCode(),
        updatedClient.getDisbursementsAmount() + disbursementsAmount,
        updatedClient.getServicesAmount() + servicesAmount,
        updatedClient.getAmount() + disbursementsAmount + servicesAmount
    );
  }
}
