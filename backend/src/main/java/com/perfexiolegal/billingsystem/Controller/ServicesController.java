package com.perfexiolegal.billingsystem.Controller;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.ApiResponse;
import com.perfexiolegal.billingsystem.Model.Services;
import com.perfexiolegal.billingsystem.Model.ServicesWithoutId;
import com.perfexiolegal.billingsystem.Model.ServicesWithoutIdAndAmount;
import com.perfexiolegal.billingsystem.Service.ServicesService;
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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Controller
@RequestMapping("/backend")
public class ServicesController {

  final Logger logger = LoggerFactory.getLogger(ServicesController.class);

  @Autowired
  ServicesService servicesService;

  @Autowired
  ServicesTransformer servicesTransformer;

  @GetMapping(value = "/services")
  public ResponseEntity<ApiResponse> getAllServices() {
    try {
      logger.info("retrieving all services from controller");
      Optional<List<Services>> listOfServices = servicesService.getAllServices();
      List<Services> services = listOfServices.get();
      if (services.isEmpty()) {
        return new ResponseEntity<>(ApiResponse.builder()
            .message("No services found")
            .success(true)
            .data(services)
            .build(), HttpStatus.OK);
      }
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Services retrieved successfully")
          .success(true)
          .data(services)
          .build(), HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Internal server error")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/services/case={caseID}")
  public ResponseEntity<ApiResponse> getServicesForCase(@PathVariable("caseID") String caseID) {
    try {
      logger.info("retrieving service from controller with caseID: " + caseID);
      Optional<List<Services>> listOfServices = servicesService.getServicesForCase(caseID);
      List<Services> services = listOfServices.get();
      if (services.isEmpty()) {
        return new ResponseEntity<>(ApiResponse.builder()
            .message("No services found for case: " + caseID)
            .success(true)
            .data(services)
            .build(), HttpStatus.OK);
      }
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Services retrieved successfully")
          .success(true)
          .data(services)
          .build(), HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Internal server error")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/services={serviceID}")
  public ResponseEntity<ApiResponse> getServicesFromId(@PathVariable("serviceID") String serviceID) {
    try {
      logger.info("retrieving service from controller with serviceID: " + serviceID);
      Optional<Services> retrievedService = servicesService.getServiceFromId(serviceID);
      if (retrievedService.isEmpty()) {
        return new ResponseEntity<>(ApiResponse.builder()
            .message("No service found with ID: " + serviceID)
            .success(true)
            .build(), HttpStatus.OK);
      }
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Service retrieved successfully")
          .success(true)
          .data(retrievedService.get())
          .build(), HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Internal server error")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/services/client={clientID}")
  public ResponseEntity<ApiResponse> getServicesForClient(@PathVariable("clientID") String clientID) {
    try{
      logger.info("retrieving service from controller with clientID: " + clientID);
      Optional<List<Services>> listOfServices = servicesService.getServicesForClient(clientID);
      List<Services> services = listOfServices.get();
      if (services.isEmpty()) {
        return new ResponseEntity<>(ApiResponse.builder()
            .message("No services found for client: " + clientID)
            .success(true)
            .data(services)
            .build(), HttpStatus.OK);
      }
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Services retrieved successfully")
          .success(true)
          .data(services)
          .build(), HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Internal server error")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(value = "/services", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApiResponse> createNewService(@RequestBody ServicesWithoutIdAndAmount serviceJSON) {
    try {
      logger.info("Creating product with name: " + serviceJSON.getService());
      Services service = servicesTransformer.fromJsonWithoutIDAndAmount(serviceJSON);
      servicesService.postServices(service);
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Service was created successfully.")
          .success(true)
          .data(service)
          .build(), HttpStatus.CREATED);
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Internal server error")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping(value = "/services={serviceID}")
  public ResponseEntity<ApiResponse> updateService(@PathVariable("serviceID") String serviceID,
                                              @RequestBody ServicesWithoutId serviceJSONWithoutID) {
    try {
      logger.info("check existence of service with service ID: " + serviceID);
      Optional<Services> findService = servicesService.getServiceFromId(serviceID);
      if (findService.isPresent()) {
        logger.info("update service with ID: " + serviceID);
        Services service = servicesTransformer.update(serviceJSONWithoutID, serviceID);
        servicesService.updateServices(service);
        return new ResponseEntity<>(ApiResponse.builder()
            .message("Service was updated successfully.")
            .success(true)
            .data(service)
            .build(), HttpStatus.OK);
      } else{
        return new ResponseEntity<>(ApiResponse.builder()
            .message("Cannot find product with id=" + serviceID)
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

  @DeleteMapping(value = "/services={serviceID}")
  public ResponseEntity<ApiResponse> deleteService(@PathVariable("serviceID") String serviceID) {
    try {
      logger.info("deleting service with ID: " + serviceID);
      int result = servicesService.deleteById(serviceID);
      if (result == 0) {
        return new ResponseEntity<>(ApiResponse.builder()
            .message("Cannot find service with id= " + serviceID)
            .success(false)
            .build(), HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Service was deleted successfully.")
          .success(true)
          .build(), HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Cannot delete product.")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping(value = "/services/case={caseID}")
  public ResponseEntity<ApiResponse> deleteByCase(@PathVariable("caseID") String caseID) {
    try {
      logger.info("delete services for case: " + caseID);
      int result = servicesService.deleteByCase(caseID);
      if (result == 0) {
        return new ResponseEntity<>(ApiResponse.builder()
            .message("Cannot find service with case id= " + caseID)
            .success(false)
            .build(), HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Deleted services for the particular case successfully.")
          .success(true)
          .build(), HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Cannot delete products.")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
