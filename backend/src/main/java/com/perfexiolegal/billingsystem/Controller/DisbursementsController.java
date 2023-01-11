package com.perfexiolegal.billingsystem.Controller;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Controller
public class DisbursementsController {

  final Logger logger = LoggerFactory.getLogger(DisbursementsController.class);

  @Autowired
  DisbursementsService disbursementsService;

  @Autowired
  DisbursementsTransformer disbursementsTransformer;

  @GetMapping(value = "/disbursements")
  public ResponseEntity<List<Disbursements>> getAllDisbursements() {
    try {
      logger.info("retrieving all disbursements from controller");
      Optional<List<Disbursements>> listOfDisbursements = disbursementsService.getAllDisbursements();
      List<Disbursements> disbursements = listOfDisbursements.get();
      if (disbursements.size() == 0) {
        Map<String, Object> message = new HashMap<>();
        message.put("message", "No data for the request");
        return new ResponseEntity(List.of(message), HttpStatus.OK);
      }
      return new ResponseEntity(listOfDisbursements, HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/disbursements={disbursementID}")
  public ResponseEntity<Disbursements> getDisbursementForId(@PathVariable("disbursementID") UUID disbursementID) {
    try {
      logger.info("retrieving disbursement from controller with disbursementID: " + disbursementID);
      Disbursements disbursement = disbursementsService.getDisbursementsById(disbursementID).get();
      return new ResponseEntity(disbursement, HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/disbursements/case={caseID}")
  public ResponseEntity<List<Disbursements>> getDisbursementsForCase(@PathVariable("caseID") String caseID) {
    try {
      logger.info("retrieving disbursement from controller with caseID: " + caseID);
      Optional<List<Disbursements>> listOfDisbursements = disbursementsService.getDisbursementsByCaseId(caseID);
      List<Disbursements> disbursements = listOfDisbursements.get();
      if (disbursements.size() == 0) {
        Map<String, Object> message = new HashMap<>();
        message.put("message", "No data for the request");
        return new ResponseEntity(List.of(message), HttpStatus.OK);
      }
      return new ResponseEntity(listOfDisbursements, HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/disbursements/client={clientID}")
  public ResponseEntity<Disbursements> getDisbursementsForClient(@PathVariable("clientID") String clientID) {
    try{
      logger.info("retrieving disbursement from controller with clientID: " + clientID);
      Optional<List<Disbursements>> listOfDisbursements = disbursementsService.getDisbursementsByClientId(clientID);
      List<Disbursements> disbursements = listOfDisbursements.get();
      if (disbursements.size() == 0) {
        Map<String, Object> message = new HashMap<>();
        message.put("message", "No data for the request");
        return new ResponseEntity(List.of(message), HttpStatus.OK);
      }
      return new ResponseEntity(listOfDisbursements, HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(value = "/disbursements", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> createNewService(@RequestBody DisbursementsWithoutId disbursementJSON) {
    try {
      logger.info("Creating disbursement with name: " + disbursementJSON.getDisbursement());
      Disbursements disbursement = disbursementsTransformer.postTransformer(disbursementJSON);
      disbursementsService.postDisbursements(disbursement);
      return new ResponseEntity<>("Disbursement was created successfully.", HttpStatus.CREATED);
    } catch (ServiceException e) {
      return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping(value = "/disbursements={disbursementID}")
  public ResponseEntity<String> updateService(@PathVariable("disbursementID") UUID disbursementID,
                                              @RequestBody DisbursementsWithoutId disbursementJSONWithoutID) {
    try {
      logger.info("check existence of disbursement with disbursement ID: " + disbursementID);
      Optional<Disbursements> findDisbursement = disbursementsService.getDisbursementsById(disbursementID);
      if (findDisbursement.isPresent()) {
        logger.info("update disbursement with ID: " + findDisbursement);
        Disbursements disbursement = disbursementsTransformer.update(disbursementJSONWithoutID, disbursementID);
        disbursementsService.updateDisbursements(disbursement);
        return new ResponseEntity<>("Disbursement was updated successfully.", HttpStatus.OK);
      } else{
        return new ResponseEntity<>("Cannot find disbursement with id=" + disbursementID, HttpStatus.NOT_FOUND);
      }
    } catch (ServiceException e) {
      return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping(value = "/disbursements={disbursementID}")
  public ResponseEntity<String> deleteService(@PathVariable("disbursementID") UUID disbursementID) {
    try {
      logger.info("deleting disbursement with ID: " + disbursementID);
      int result = disbursementsService.deleteDisbursementById(disbursementID);
      if (result == 0) {
        return new ResponseEntity<>("Cannot find disbursement with id= " + disbursementID, HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>("Disbursement was deleted successfully.", HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>("Cannot delete disbursement.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
