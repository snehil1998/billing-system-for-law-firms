package com.perfexiolegal.billingsystem.Controller;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.ApiResponse;
import com.perfexiolegal.billingsystem.Model.Attorneys;
import com.perfexiolegal.billingsystem.Model.AttorneysWithoutId;
import com.perfexiolegal.billingsystem.Service.AttorneysService;
import com.perfexiolegal.billingsystem.Transformer.AttorneysTransformer;
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

@org.springframework.stereotype.Controller
@RequestMapping("/backend")
public class AttorneysController {

  final Logger logger = LoggerFactory.getLogger(AttorneysController.class);

  @Autowired
  AttorneysService attorneysService;

  @Autowired
  AttorneysTransformer attorneysTransformer;

  @GetMapping(value = "/attorneys")
  public ResponseEntity<ApiResponse> getAllAttorneys() {
    try {
      logger.info("retrieving all attorneys from controller");
      Optional<List<Attorneys>> listOfAttorneys = attorneysService.getAllAttorneys();
      List<Attorneys> attorneys = listOfAttorneys.get();
      if (attorneys.isEmpty()) {
        return new ResponseEntity<>(ApiResponse.builder()
            .message("No attorneys found")
            .success(true)
            .data(attorneys)
            .build(), HttpStatus.OK);
      }
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Attorneys retrieved successfully")
          .success(true)
          .data(attorneys)
          .build(), HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Internal server error")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/attorneys={attorneyID}")
  public ResponseEntity<ApiResponse> getAttorneysForCase(@PathVariable("attorneyID") String attorneyID) {
    try {
      logger.info("retrieving attorney from controller with attorneyID: " + attorneyID);
      Optional<Attorneys> retrievedAttorneys = attorneysService.getAttorneyById(attorneyID);
      if (retrievedAttorneys.isEmpty()) {
        return new ResponseEntity<>(ApiResponse.builder()
            .message("No attorney found with ID: " + attorneyID)
            .success(true)
            .build(), HttpStatus.OK);
      }
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Attorney retrieved successfully")
          .success(true)
          .data(retrievedAttorneys.get())
          .build(), HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Internal server error")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(value = "/attorneys", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApiResponse> createNewAttorney(@RequestBody Attorneys attorney) {
    try {
      logger.info("Creating attorney with attorneyID: " + attorney.getAttorneyId());
      try{
        attorneysService.getAttorneyById(attorney.getAttorneyId()).isPresent();
        return new ResponseEntity<>(ApiResponse.builder()
            .message("Attorney with ID already exists.")
            .success(false)
            .build(), HttpStatus.GONE);
      } catch (Exception e) {
        attorneysService.postAttorneys(attorney);
        return new ResponseEntity<>(ApiResponse.builder()
            .message("Attorney was created successfully.")
            .success(true)
            .data(attorney)
            .build(), HttpStatus.CREATED);
      }
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Internal server error")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping(value = "/attorneys={attorneyID}")
  public ResponseEntity<ApiResponse> updateAttorney(@PathVariable("attorneyID") String attorneyID,
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
            return new ResponseEntity<>(ApiResponse.builder()
                .message("Client already exists in service pricing.")
                .success(false)
                .build(), HttpStatus.GONE);
          }
          updatedAttorney = attorneysTransformer.update(attorneyJSONWithoutID, attorneyID);
        }
        attorneysService.updateAttorney(updatedAttorney);
        return new ResponseEntity<>(ApiResponse.builder()
            .message("Attorney was updated successfully.")
            .success(true)
            .data(updatedAttorney)
            .build(), HttpStatus.OK);
      } else{
        return new ResponseEntity<>(ApiResponse.builder()
            .message("Cannot find attorney with id=" + attorneyID)
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

  @DeleteMapping(value = "/attorneys={attorneyID}")
  public ResponseEntity<ApiResponse> deleteService(@PathVariable("attorneyID") String attorneyID) {
    try {
      logger.info("deleting attorney with ID: " + attorneyID);
      int result = attorneysService.deleteById(attorneyID);
      if (result == 0) {
        return new ResponseEntity<>(ApiResponse.builder()
            .message("Cannot find attorney with id= " + attorneyID)
            .success(false)
            .build(), HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Attorney was deleted successfully.")
          .success(true)
          .build(), HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Cannot delete attorney.")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
