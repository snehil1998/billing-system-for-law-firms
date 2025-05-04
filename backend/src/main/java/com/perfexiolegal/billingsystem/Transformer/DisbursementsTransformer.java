package com.perfexiolegal.billingsystem.Transformer;

import com.perfexiolegal.billingsystem.Model.Disbursements;
import com.perfexiolegal.billingsystem.Model.DisbursementsWithoutId;
import org.springframework.stereotype.Component;

@Component
public class DisbursementsTransformer {

  public Disbursements update(DisbursementsWithoutId updatedDisbursement, String disbursementID) {
    return new Disbursements(
        disbursementID,
        updatedDisbursement.getCaseId(),
        updatedDisbursement.getClientId(),
        updatedDisbursement.getDisbursement(),
        updatedDisbursement.getDate(),
        updatedDisbursement.getCurrencyCode(),
        updatedDisbursement.getConversionRate(),
        updatedDisbursement.getInrAmount(),
        updatedDisbursement.getConversionAmount()
    );
  }

  public Disbursements postTransformer(DisbursementsWithoutId updatedDisbursement) {
    return new Disbursements(
        updatedDisbursement.getDisbursementId(),
        updatedDisbursement.getCaseId(),
        updatedDisbursement.getClientId(),
        updatedDisbursement.getDisbursement(),
        updatedDisbursement.getDate(),
        updatedDisbursement.getCurrencyCode(),
        updatedDisbursement.getConversionRate(),
        updatedDisbursement.getInrAmount(),
        updatedDisbursement.getConversionAmount()
    );
  }
}
