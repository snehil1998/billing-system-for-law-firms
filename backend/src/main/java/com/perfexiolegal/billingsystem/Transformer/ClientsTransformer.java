package com.perfexiolegal.billingsystem.Transformer;

import com.perfexiolegal.billingsystem.Model.Clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ClientsTransformer {

  private static final Logger logger = LoggerFactory.getLogger(ClientsTransformer.class);

  public Clients updateAmount(Clients updatedClient, double disbursementsAmount, double servicesAmount) {
    logger.debug("Updating amounts for client with ID: {}", updatedClient.getClientId());
    return new Clients(
        updatedClient.getClientId(),
        updatedClient.getClientName(),
        updatedClient.getCurrencyCode(),
        updatedClient.getDisbursementsAmount() + disbursementsAmount,
        updatedClient.getServicesAmount() + servicesAmount,
        updatedClient.getAmount() + disbursementsAmount + servicesAmount
    );
  }
}
