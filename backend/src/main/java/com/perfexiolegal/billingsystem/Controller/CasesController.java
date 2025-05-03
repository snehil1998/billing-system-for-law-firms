package com.perfexiolegal.billingsystem.Controller;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.ApiResponse;
import com.perfexiolegal.billingsystem.Model.Cases;
import com.perfexiolegal.billingsystem.Model.CasesWithoutId;
import com.perfexiolegal.billingsystem.Service.CasesService;
import com.perfexiolegal.billingsystem.Transformer.CasesTransformer;
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
public class CasesController {

  final Logger logger = LoggerFactory.getLogger(CasesController.class);

  @Autowired
  CasesService casesService;

  @Autowired
  CasesTransformer casesTransformer;

  @GetMapping(value = "/cases")
  public ResponseEntity<ApiResponse> getAllCases() {
    try {
      logger.info("retrieving all cases from controller");
      Optional<List<Cases>> listOfCases = casesService.getAllCases();
      List<Cases> cases = listOfCases.get();
      if (cases.isEmpty()) {
        return new ResponseEntity<>(ApiResponse.builder()
            .message("No cases found")
            .success(true)
            .data(cases)
            .build(), HttpStatus.OK);
      }
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Cases retrieved successfully")
          .success(true)
          .data(cases)
          .build(), HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Internal server error")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/cases={caseID}")
  public ResponseEntity<ApiResponse> getServicesForCase(@PathVariable("caseID") String caseID) {
    try {
      logger.info("retrieving case from controller with caseID: " + caseID);
      Optional<Cases> retrievedCase = casesService.getCaseById(caseID);
      if (retrievedCase.isEmpty()) {
        return new ResponseEntity<>(ApiResponse.builder()
            .message("No case found with ID: " + caseID)
            .success(true)
            .build(), HttpStatus.OK);
      }
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Case retrieved successfully")
          .success(true)
          .data(retrievedCase.get())
          .build(), HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Internal server error")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(value = "/cases", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApiResponse> createNewCase(@RequestBody Cases newCase) {
    try {
      logger.info("Creating case with name: " + newCase.getCaseId());
      try {
        casesService.getCaseById(newCase.getCaseId()).isPresent();
        return new ResponseEntity<>(ApiResponse.builder()
            .message("Case with ID already exists.")
            .success(false)
            .build(), HttpStatus.GONE);
      } catch (Exception e) {
        casesService.postCases(newCase);
        return new ResponseEntity<>(ApiResponse.builder()
            .message("Case was created successfully.")
            .success(true)
            .data(newCase)
            .build(), HttpStatus.CREATED);
      }
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Internal server error")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping(value = "/cases={caseID}")
  public ResponseEntity<ApiResponse> updateCase(@PathVariable("caseID") String caseID,
                                              @RequestBody CasesWithoutId caseJSONWithoutID) {
    try {
      logger.info("check existence of case with case ID: " + caseID);
      Optional<Cases> findCase = casesService.getCaseById(caseID);
      if (findCase.isPresent()) {
        logger.info("update case with ID: " + caseID);
        Cases updatedCase = casesTransformer.update(caseJSONWithoutID, caseID);
        casesService.updateCases(updatedCase);
        return new ResponseEntity<>(ApiResponse.builder()
            .message("Case was updated successfully.")
            .success(true)
            .data(updatedCase)
            .build(), HttpStatus.OK);
      } else{
        return new ResponseEntity<>(ApiResponse.builder()
            .message("Cannot find case with id=" + caseID)
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

  @DeleteMapping(value = "/cases={caseID}")
  public ResponseEntity<ApiResponse> deleteService(@PathVariable("caseID") String caseID) {
    try {
      logger.info("deleting case with ID: " + caseID);
      int result = casesService.deleteById(caseID);
      if (result == 0) {
        return new ResponseEntity<>(ApiResponse.builder()
            .message("Cannot find case with id= " + caseID)
            .success(false)
            .build(), HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Case was deleted successfully.")
          .success(true)
          .build(), HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Cannot delete case.")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
