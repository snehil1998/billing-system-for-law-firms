package com.perfexiolegal.billingsystem.Controller;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Controller
public class ServicesController {

  final Logger logger = LoggerFactory.getLogger(ServicesController.class);

  @Autowired
  ServicesService servicesService;

  @Autowired
  ServicesTransformer servicesTransformer;

  @GetMapping(value = "/services")
  public ResponseEntity<List<Services>> getAllServices() {
    try {
      logger.info("retrieving all services from controller");
      Optional<List<Services>> listOfServices = servicesService.getAllServices();
      List<Services> services = listOfServices.get();
      if (services.size() == 0) {
        Map<String, Object> message = new HashMap<>();
        message.put("message", "No data for the request");
        return new ResponseEntity(List.of(message), HttpStatus.OK);
      }
      return new ResponseEntity(listOfServices, HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/services/case={caseID}")
  public ResponseEntity<List<Services>> getServicesForCase(@PathVariable("caseID") String caseID) {
    try {
      logger.info("retrieving service from controller with caseID: " + caseID);
      Optional<List<Services>> listOfServices = servicesService.getServicesForCase(caseID);
      List<Services> services = listOfServices.get();
      if (services.size() == 0) {
        Map<String, Object> message = new HashMap<>();
        message.put("message", "No data for the request");
        return new ResponseEntity(List.of(message), HttpStatus.OK);
      }
      return new ResponseEntity(listOfServices, HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/services={serviceID}")
  public ResponseEntity<Services> getServicesFromId(@PathVariable("serviceID") UUID serviceID) {
    try {
      logger.info("retrieving service from controller with serviceID: " + serviceID);
      Services service = servicesService.getServiceFromId(serviceID).get();
      return new ResponseEntity(service, HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/services/client={clientID}")
  public ResponseEntity<Services> getServicesForClient(@PathVariable("clientID") String clientID) {
    try{
      logger.info("retrieving service from controller with clientID: " + clientID);
      Optional<List<Services>> listOfServices = servicesService.getServicesForClient(clientID);
      List<Services> services = listOfServices.get();
      if (services.size() == 0) {
        Map<String, Object> message = new HashMap<>();
        message.put("message", "No data for the request");
        return new ResponseEntity(List.of(message), HttpStatus.OK);
      }
      return new ResponseEntity(listOfServices, HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(value = "/services", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> createNewService(@RequestBody ServicesWithoutIdAndAmount serviceJSON) {
    try {
      logger.info("Creating product with name: " + serviceJSON.getService());
      Services service = servicesTransformer.fromJsonWithoutIDAndAmount(serviceJSON);
      servicesService.postServices(service);
      return new ResponseEntity<>("Service was created successfully.", HttpStatus.CREATED);
    } catch (ServiceException e) {
      return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping(value = "/services={serviceID}")
  public ResponseEntity<String> updateService(@PathVariable("serviceID") UUID serviceID,
                                              @RequestBody ServicesWithoutId serviceJSONWithoutID) {
    try {
      logger.info("check existence of service with service ID: " + serviceID);
      Optional<Services> findService = servicesService.getServiceFromId(serviceID);
      if (findService.isPresent()) {
        logger.info("update service with ID: " + serviceID);
        Services service = servicesTransformer.update(serviceJSONWithoutID, serviceID);
        servicesService.updateServices(service);
        return new ResponseEntity<>("Service was updated successfully.", HttpStatus.OK);
      } else{
        return new ResponseEntity<>("Cannot find product with id=" + serviceID, HttpStatus.NOT_FOUND);
      }
    } catch (ServiceException e) {
      return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping(value = "/services={serviceID}")
  public ResponseEntity<String> deleteService(@PathVariable("serviceID") UUID serviceID) {
    try {
      logger.info("deleting service with ID: " + serviceID);
      int result = servicesService.deleteById(serviceID);
      if (result == 0) {
        return new ResponseEntity<>("Cannot find service with id= " + serviceID, HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>("Service was deleted successfully.", HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>("Cannot delete product.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping(value = "/services/case={caseID}")
  public ResponseEntity<String> deleteByCase(@PathVariable("caseID") String caseID) {
    try {
      logger.info("delete services for case: " + caseID);
      int result = servicesService.deleteByCase(caseID);
      if (result == 0) {
        return new ResponseEntity<>("Cannot find service with case id= " + caseID, HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>("Deleted services for the particular case successfully.", HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>("Cannot delete products.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
