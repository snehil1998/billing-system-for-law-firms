package com.perfexiolegal.billingsystem.Controller;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.ApiResponse;
import com.perfexiolegal.billingsystem.Model.Disbursements;
import com.perfexiolegal.billingsystem.Model.DisbursementsWithoutId;
import com.perfexiolegal.billingsystem.Service.DisbursementsService;
import com.perfexiolegal.billingsystem.Transformer.DisbursementsTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Controller
@RequestMapping("/backend")
public class DisbursementsController {

  final Logger logger = LoggerFactory.getLogger(DisbursementsController.class);

  @Autowired
  DisbursementsService disbursementsService;

  @Autowired
  DisbursementsTransformer disbursementsTransformer;

  @GetMapping(value = "/disbursements")
  public ResponseEntity<ApiResponse> getAllDisbursements() {
    try {
      logger.info("retrieving all disbursements from controller");
      Optional<List<Disbursements>> listOfDisbursements = disbursementsService.getAllDisbursements();
      List<Disbursements> disbursements = listOfDisbursements.get();
      if (disbursements.isEmpty()) {
        return new ResponseEntity<>(ApiResponse.builder()
            .message("No disbursements found")
            .success(true)
            .data(disbursements)
            .build(), HttpStatus.OK);
      }
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Disbursements retrieved successfully")
          .success(true)
          .data(disbursements)
          .build(), HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Internal server error")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/disbursements={disbursementID}")
  public ResponseEntity<ApiResponse> getDisbursementForId(@PathVariable("disbursementID") UUID disbursementID) {
    try {
      logger.info("retrieving disbursement from controller with disbursementID: " + disbursementID);
      Optional<Disbursements> retrievedDisbursement = disbursementsService.getDisbursementsById(disbursementID);
      if (retrievedDisbursement.isEmpty()) {
        return new ResponseEntity<>(ApiResponse.builder()
            .message("No disbursement found with ID: " + disbursementID)
            .success(true)
            .build(), HttpStatus.OK);
      }
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Disbursement retrieved successfully")
          .success(true)
          .data(retrievedDisbursement.get())
          .build(), HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Internal server error")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/disbursements/case={caseID}")
  public ResponseEntity<ApiResponse> getDisbursementsForCase(@PathVariable("caseID") String caseID) {
    try {
      logger.info("retrieving disbursement from controller with caseID: " + caseID);
      Optional<List<Disbursements>> listOfDisbursements = disbursementsService.getDisbursementsByCaseId(caseID);
      List<Disbursements> disbursements = listOfDisbursements.get();
      if (disbursements.isEmpty()) {
        return new ResponseEntity<>(ApiResponse.builder()
            .message("No disbursements found for case: " + caseID)
            .success(true)
            .data(disbursements)
            .build(), HttpStatus.OK);
      }
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Disbursements retrieved successfully")
          .success(true)
          .data(disbursements)
          .build(), HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Internal server error")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/disbursements/client={clientID}")
  public ResponseEntity<ApiResponse> getDisbursementsForClient(@PathVariable("clientID") String clientID) {
    try{
      logger.info("retrieving disbursement from controller with clientID: " + clientID);
      Optional<List<Disbursements>> listOfDisbursements = disbursementsService.getDisbursementsByClientId(clientID);
      List<Disbursements> disbursements = listOfDisbursements.get();
      if (disbursements.isEmpty()) {
        return new ResponseEntity<>(ApiResponse.builder()
            .message("No disbursements found for client: " + clientID)
            .success(true)
            .data(disbursements)
            .build(), HttpStatus.OK);
      }
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Disbursements retrieved successfully")
          .success(true)
          .data(disbursements)
          .build(), HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Internal server error")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(value = "/disbursements", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApiResponse> createNewService(@RequestBody DisbursementsWithoutId disbursementJSON) {
    try {
      logger.info("Creating disbursement with name: " + disbursementJSON.getDisbursement());
      Disbursements disbursement = disbursementsTransformer.postTransformer(disbursementJSON);
      disbursementsService.postDisbursements(disbursement);
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Disbursement was created successfully.")
          .success(true)
          .data(disbursement)
          .build(), HttpStatus.CREATED);
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Internal server error")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping(value = "/disbursements={disbursementID}")
  public ResponseEntity<ApiResponse> updateService(@PathVariable("disbursementID") UUID disbursementID,
                                              @RequestBody DisbursementsWithoutId disbursementJSONWithoutID) {
    try {
      logger.info("check existence of disbursement with disbursement ID: " + disbursementID);
      Optional<Disbursements> findDisbursement = disbursementsService.getDisbursementsById(disbursementID);
      if (findDisbursement.isPresent()) {
        logger.info("update disbursement with ID: " + findDisbursement);
        Disbursements disbursement = disbursementsTransformer.update(disbursementJSONWithoutID, disbursementID);
        disbursementsService.updateDisbursements(disbursement);
        return new ResponseEntity<>(ApiResponse.builder()
            .message("Disbursement was updated successfully.")
            .success(true)
            .data(disbursement)
            .build(), HttpStatus.OK);
      } else{
        return new ResponseEntity<>(ApiResponse.builder()
            .message("Cannot find disbursement with id=" + disbursementID)
            .success(false)
            .build(), HttpStatus.NOT_FOUND);
      }
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Internal server error")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping(value = "/disbursements={disbursementID}")
  public ResponseEntity<ApiResponse> deleteService(@PathVariable("disbursementID") UUID disbursementID) {
    try {
      logger.info("deleting disbursement with ID: " + disbursementID);
      int result = disbursementsService.deleteDisbursementById(disbursementID);
      if (result == 0) {
        return new ResponseEntity<>(ApiResponse.builder()
            .message("Cannot find disbursement with id= " + disbursementID)
            .success(false)
            .build(), HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Disbursement was deleted successfully.")
          .success(true)
          .build(), HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Cannot delete disbursement.")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
