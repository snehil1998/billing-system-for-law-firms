package com.perfexiolegal.billingsystem.Transformer;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.DisbursementRequest;
import com.perfexiolegal.billingsystem.Model.DisbursementRequestDto;


import org.springframework.stereotype.Component;

@Component
public class DisbursementsTransformer {
  public DisbursementRequestDto ToDisbursementRequestDto(DisbursementRequest request) throws ServiceException {
    return DisbursementRequestDto.builder()
      .caseId(request.getCaseId())
      .clientId(request.getClientId())
      .disbursement(request.getDisbursement())
      .description(request.getDescription())
      .date(request.getDate())
      .currencyCode(request.getCurrencyCode())
      .conversionRate(request.getConversionRate())
      .inrAmount(request.getInrAmount())
      .conversionAmount(request.getConversionAmount())
      .build();
  }
}
