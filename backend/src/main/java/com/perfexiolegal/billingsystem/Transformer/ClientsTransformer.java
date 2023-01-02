package com.perfexiolegal.billingsystem.Transformer;

import com.perfexiolegal.billingsystem.Model.Clients;
import com.perfexiolegal.billingsystem.Model.ClientsWithoutId;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ClientsTransformer {

  public Clients update(ClientsWithoutId client, String clientID) {
    return new Clients(
        clientID,
        client.getClientName(),
        client.getCurrencyCode(),
        client.getAmount()
    );
  }
}
