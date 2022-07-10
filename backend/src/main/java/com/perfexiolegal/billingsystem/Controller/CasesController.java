package com.perfexiolegal.billingsystem.Controller;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.Cases;
import com.perfexiolegal.billingsystem.Model.CasesWithoutId;
import com.perfexiolegal.billingsystem.Model.Services;
import com.perfexiolegal.billingsystem.Model.ServicesWithoutId;
import com.perfexiolegal.billingsystem.Service.CasesService;
import com.perfexiolegal.billingsystem.Service.ServicesService;
import com.perfexiolegal.billingsystem.Transformer.CasesTransformer;
import com.perfexiolegal.billingsystem.Transformer.ServicesTransformer;
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
public class CasesController {

  final Logger logger = LoggerFactory.getLogger(CasesController.class);

  @Autowired
  CasesService casesService;

  @Autowired
  CasesTransformer casesTransformer;

  @GetMapping(value = "/cases")
  public ResponseEntity<List<Cases>> getAllCases() {
    try {
      logger.info("retrieving all cases from controller");
      Optional<List<Cases>> listOfCases = casesService.getAllCases();
      List<Cases> cases = listOfCases.get();
      if (cases.size() == 0) {
        Map<String, Object> message = new HashMap<>();
        message.put("message", "No data for the request");
        return new ResponseEntity(List.of(message), HttpStatus.OK);
      }
      return new ResponseEntity(listOfCases, HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/cases={caseID}")
  public ResponseEntity<Cases> getServicesForCase(@PathVariable("caseID") UUID caseID) {
    try {
      logger.info("retrieving case from controller with caseID: " + caseID);
      Optional<Cases> retrievedCase = casesService.getCaseById(caseID);
      if (retrievedCase.isEmpty()) {
        Map<String, Object> message = new HashMap<>();
        message.put("message", "No data for the request");
        return new ResponseEntity(message, HttpStatus.OK);
      }
      return new ResponseEntity(retrievedCase.get(), HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(value = "/cases", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> createNewCase(@RequestBody Cases newCase) {
    try {
      logger.info("Creating case with name: " + newCase.getCaseId());
      casesService.postCases(newCase);
      return new ResponseEntity<>("Case was created successfully.", HttpStatus.CREATED);
    } catch (ServiceException e) {
      return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping(value = "/cases={caseID}")
  public ResponseEntity<String> updateCase(@PathVariable("caseID") UUID caseID,
                                              @RequestBody CasesWithoutId caseJSONWithoutID) {
    try {
      logger.info("check existence of case with case ID: " + caseID);
      Optional<Cases> findCase = casesService.getCaseById(caseID);
      if (findCase.isPresent()) {
        logger.info("update case with ID: " + caseID);
        Cases updatedCase = casesTransformer.update(caseJSONWithoutID, caseID);
        casesService.updateCases(updatedCase);
        return new ResponseEntity<>("Case was updated successfully.", HttpStatus.OK);
      } else{
        return new ResponseEntity<>("Cannot find case with id=" + caseID, HttpStatus.NOT_FOUND);
      }
    } catch (ServiceException e) {
      return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping(value = "/cases={caseID}")
  public ResponseEntity<String> deleteService(@PathVariable("caseID") UUID caseID) {
    try {
      logger.info("deleting case with ID: " + caseID);
      int result = casesService.deleteById(caseID);
      if (result == 0) {
        return new ResponseEntity<>("Cannot find case with id= " + caseID, HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>("Case was deleted successfully.", HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>("Cannot delete case.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
