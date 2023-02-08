package com.perfexiolegal.billingsystem.Controller;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.Attorneys;
import com.perfexiolegal.billingsystem.Model.AttorneysWithoutId;
import com.perfexiolegal.billingsystem.Model.Cases;
import com.perfexiolegal.billingsystem.Model.Clients;
import com.perfexiolegal.billingsystem.Model.ClientsWithoutId;
import com.perfexiolegal.billingsystem.Service.AttorneysService;
import com.perfexiolegal.billingsystem.Service.ClientsService;
import com.perfexiolegal.billingsystem.Transformer.AttorneysTransformer;
import com.perfexiolegal.billingsystem.Transformer.ClientsTransformer;
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
import java.util.stream.Collectors;

@org.springframework.stereotype.Controller
public class AttorneysController {

  final Logger logger = LoggerFactory.getLogger(AttorneysController.class);

  @Autowired
  AttorneysService attorneysService;

  @Autowired
  AttorneysTransformer attorneysTransformer;

  @GetMapping(value = "/attorneys")
  public ResponseEntity<List<Attorneys>> getAllAttorneys() {
    try {
      logger.info("retrieving all attorneys from controller");
      Optional<List<Attorneys>> listOfAttorneys = attorneysService.getAllAttorneys();
      List<Attorneys> attorneys = listOfAttorneys.get();
      if (attorneys.size() == 0) {
        Map<String, Object> message = new HashMap<>();
        message.put("message", "No data for the request");
        return new ResponseEntity(List.of(message), HttpStatus.OK);
      }
      return new ResponseEntity(listOfAttorneys, HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/attorneys={attorneyID}")
  public ResponseEntity<Attorneys> getAttorneysForCase(@PathVariable("attorneyID") String attorneyID) {
    try {
      logger.info("retrieving attorney from controller with attorneyID: " + attorneyID);
      Optional<Attorneys> retrievedAttorneys = attorneysService.getAttorneyById(attorneyID);
      if (retrievedAttorneys.isEmpty()) {
        Map<String, Object> message = new HashMap<>();
        message.put("message", "No data for the request");
        return new ResponseEntity(message, HttpStatus.OK);
      }
      return new ResponseEntity(retrievedAttorneys.get(), HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(value = "/attorneys", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> createNewAttorney(@RequestBody Attorneys attorney) {
    try {
      logger.info("Creating attorney with attorneyID: " + attorney.getAttorneyId());
      try{
        attorneysService.getAttorneyById(attorney.getAttorneyId()).isPresent();
        return new ResponseEntity<>("Attorney with ID already exists.", HttpStatus.GONE);
      } catch (Exception e) {
        attorneysService.postAttorneys(attorney);
        return new ResponseEntity<>("Attorney was created successfully.", HttpStatus.CREATED);
      }
    } catch (ServiceException e) {
      return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping(value = "/attorneys={attorneyID}")
  public ResponseEntity<String> updateAttorney(@PathVariable("attorneyID") String attorneyID,
                                              @RequestBody AttorneysWithoutId attorneyJSONWithoutID) {
    try {
      logger.info("check existence of attorney with attorney ID: " + attorneyID);
      Optional<Attorneys> findAttorney = attorneysService.getAttorneyById(attorneyID);
      if (findAttorney.isPresent()) {
        logger.info("update attorney with ID: " + attorneyID);
        Attorneys updatedAttorney;
        if (attorneyJSONWithoutID.getServicePricing().get(0).getPrice() < 0) {
          updatedAttorney = attorneysTransformer.deleteServicePrice(findAttorney.get(),
              attorneyJSONWithoutID.getServicePricing().get(0).getClientId());
        } else {
          String clientId = attorneyJSONWithoutID.getServicePricing().get(0).getClientId();
          if(findAttorney.get().getServicePricing().stream()
              .anyMatch(pricing -> pricing.getClientId().equals(clientId))) {
            return new ResponseEntity<>("Client already exists in service pricing.", HttpStatus.GONE);
          }
          updatedAttorney = attorneysTransformer.update(attorneyJSONWithoutID, attorneyID);
        }
        attorneysService.updateAttorney(updatedAttorney);
        return new ResponseEntity<>("Attorney was updated successfully.", HttpStatus.OK);
      } else{
        return new ResponseEntity<>("Cannot find attorney with id=" + attorneyID, HttpStatus.NOT_FOUND);
      }
    } catch (ServiceException e) {
      return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping(value = "/attorneys={attorneyID}")
  public ResponseEntity<String> deleteService(@PathVariable("attorneyID") String attorneyID) {
    try {
      logger.info("deleting attorney with ID: " + attorneyID);
      int result = attorneysService.deleteById(attorneyID);
      if (result == 0) {
        return new ResponseEntity<>("Cannot find attorney with id= " + attorneyID, HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>("Attorney was deleted successfully.", HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>("Cannot delete attorney.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
