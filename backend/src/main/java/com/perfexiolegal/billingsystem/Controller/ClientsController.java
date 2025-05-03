package com.perfexiolegal.billingsystem.Controller;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.ApiResponse;
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

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Controller
@RequestMapping("/backend")
public class ClientsController {

  final Logger logger = LoggerFactory.getLogger(ClientsController.class);

  @Autowired
  ClientsService clientsService;

  @Autowired
  ClientsTransformer clientsTransformer;

  @GetMapping(value = "/clients")
  public ResponseEntity<ApiResponse> getAllClients() {
    try {
      logger.info("retrieving all clients from controller");
      Optional<List<Clients>> listOfClients = clientsService.getAllClients();
      List<Clients> clients = listOfClients.get();
      if (clients.isEmpty()) {
        return new ResponseEntity<>(ApiResponse.builder()
            .message("No clients found")
            .success(true)
            .data(clients)
            .build(), HttpStatus.OK);
      }
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Clients retrieved successfully")
          .success(true)
          .data(clients)
          .build(), HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Internal server error")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/clients={clientID}")
  public ResponseEntity<ApiResponse> getServicesForCase(@PathVariable("clientID") String clientID) {
    try {
      logger.info("retrieving client from controller with clientID: " + clientID);
      Optional<Clients> retrievedClients = clientsService.getClientById(clientID);
      if (retrievedClients.isEmpty()) {
        return new ResponseEntity<>(ApiResponse.builder()
            .message("No client found with ID: " + clientID)
            .success(true)
            .build(), HttpStatus.OK);
      }
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Client retrieved successfully")
          .success(true)
          .data(retrievedClients.get())
          .build(), HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Internal server error")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(value = "/clients", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApiResponse> createNewCase(@RequestBody Clients client) {
    try {
      logger.info("Creating client with clientID: " + client.getClientId());
      try {
        clientsService.getClientById(client.getClientId()).isPresent();
        return new ResponseEntity<>(ApiResponse.builder()
            .message("Client with ID already exists.")
            .success(false)
            .build(), HttpStatus.GONE);
      } catch (Exception e) {
        clientsService.postClients(client);
        return new ResponseEntity<>(ApiResponse.builder()
            .message("Client was created successfully.")
            .success(true)
            .data(client)
            .build(), HttpStatus.CREATED);
      }
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Internal server error")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping(value = "/clients={clientID}")
  public ResponseEntity<ApiResponse> updateCase(@PathVariable("clientID") String clientID,
                                              @RequestBody ClientsWithoutId clientJSONWithoutID) {
    try {
      logger.info("check existence of client with client ID: " + clientID);
      Optional<Clients> findClient = clientsService.getClientById(clientID);
      if (findClient.isPresent()) {
        logger.info("update client with ID: " + clientID);
        Clients updatedClient = clientsTransformer.update(clientJSONWithoutID, clientID);
        clientsService.updateClient(updatedClient);
        return new ResponseEntity<>(ApiResponse.builder()
            .message("Client was updated successfully.")
            .success(true)
            .data(updatedClient)
            .build(), HttpStatus.OK);
      } else{
        return new ResponseEntity<>(ApiResponse.builder()
            .message("Cannot find client with id=" + clientID)
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

  @DeleteMapping(value = "/clients={clientID}")
  public ResponseEntity<ApiResponse> deleteService(@PathVariable("clientID") String clientID) {
    try {
      logger.info("deleting client with ID: " + clientID);
      int result = clientsService.deleteById(clientID);
      if (result == 0) {
        return new ResponseEntity<>(ApiResponse.builder()
            .message("Cannot find client with id= " + clientID)
            .success(false)
            .build(), HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Client was deleted successfully.")
          .success(true)
          .build(), HttpStatus.OK);
    } catch (ServiceException e) {
      return new ResponseEntity<>(ApiResponse.builder()
          .message("Cannot delete client.")
          .success(false)
          .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
