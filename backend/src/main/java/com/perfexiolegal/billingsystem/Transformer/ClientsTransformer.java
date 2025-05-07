package com.perfexiolegal.billingsystem.Transformer;

import com.perfexiolegal.billingsystem.Model.Clients;
import org.springframework.stereotype.Component;

@Component
public class ClientsTransformer {

  public Clients updateAmount(Clients updatedClient, double disbursementsAmount, double servicesAmount) {
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
