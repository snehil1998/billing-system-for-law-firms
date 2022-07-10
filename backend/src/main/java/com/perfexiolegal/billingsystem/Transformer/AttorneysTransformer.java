package com.perfexiolegal.billingsystem.Transformer;

import com.perfexiolegal.billingsystem.Model.Attorneys;
import com.perfexiolegal.billingsystem.Model.AttorneysWithoutId;
import com.perfexiolegal.billingsystem.Model.Clients;
import com.perfexiolegal.billingsystem.Model.ClientsWithoutId;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AttorneysTransformer {

  public Attorneys update(AttorneysWithoutId attorney, UUID attorneyID) {
    return new Attorneys(
        attorneyID,
        attorney.getFirstName(),
        attorney.getLastName()
    );
  }
}
