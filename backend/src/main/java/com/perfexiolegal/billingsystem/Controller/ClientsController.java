package com.perfexiolegal.billingsystem.Controller;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.Cases;
import com.perfexiolegal.billingsystem.Model.Clients;
import com.perfexiolegal.billingsystem.Model.ClientsWithoutId;
import com.perfexiolegal.billingsystem.Service.ClientsService;
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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Controller
@RequestMapping("/backend")
public class ClientsController {

  final Logger logger = LoggerFactory.getLogger(ClientsController.class);

  @Autowired
  ClientsService clientsService;

  @Autowired
  ClientsTransformer clientsTransformer;

  @GetMapping(value = "/clients")
  public ResponseEntity<List<Cases>> getAllClients() {
    try {
      logger.info("retrieving all clients from controller");
      Optional<List<Clients>> listOfClients = clientsService.getAllClients();
      List<Clients> clients = listOfClients.get();
      if (clients.size() == 0) {
        return new ResponseEntity(HttpStatus.OK);
      }
      return new ResponseEntity(listOfClients, HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/clients={clientID}")
  public ResponseEntity<Cases> getServicesForCase(@PathVariable("clientID") String clientID) {
    try {
      logger.info("retrieving client from controller with clientID: " + clientID);
      Optional<Clients> retrievedClients = clientsService.getClientById(clientID);
      if (retrievedClients.isEmpty()) {
        return new ResponseEntity(HttpStatus.OK);
      }
      return new ResponseEntity(retrievedClients.get(), HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(value = "/clients", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> createNewCase(@RequestBody Clients client) {
    try {
      logger.info("Creating client with clientID: " + client.getClientId());
      try {
        clientsService.getClientById(client.getClientId()).isPresent();
        return new ResponseEntity<>("Client with ID already exists.", HttpStatus.GONE);
      } catch (Exception e) {
        clientsService.postClients(client);
        return new ResponseEntity<>("Client was created successfully.", HttpStatus.CREATED);
      }
    } catch (ServiceException e) {
      return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping(value = "/clients={clientID}")
  public ResponseEntity<String> updateCase(@PathVariable("clientID") String clientID,
                                              @RequestBody ClientsWithoutId clientJSONWithoutID) {
    try {
      logger.info("check existence of client with client ID: " + clientID);
      Optional<Clients> findClient = clientsService.getClientById(clientID);
      if (findClient.isPresent()) {
        logger.info("update client with ID: " + clientID);
        Clients updatedClient = clientsTransformer.update(clientJSONWithoutID, clientID);
        clientsService.updateClient(updatedClient);
        return new ResponseEntity<>("Client was updated successfully.", HttpStatus.OK);
      } else{
        return new ResponseEntity<>("Cannot find client with id=" + clientID, HttpStatus.NOT_FOUND);
      }
    } catch (ServiceException e) {
      return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping(value = "/clients={clientID}")
  public ResponseEntity<String> deleteService(@PathVariable("clientID") String clientID) {
    try {
      logger.info("deleting client with ID: " + clientID);
      int result = clientsService.deleteById(clientID);
      if (result == 0) {
        return new ResponseEntity<>("Cannot find client with id= " + clientID, HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>("Client was deleted successfully.", HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>("Cannot delete client.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
