package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.Clients;
import com.perfexiolegal.billingsystem.Repository.ClientsRepository;
import com.perfexiolegal.billingsystem.Repository.ServicesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Service
public class ClientsService {

  @Autowired
  ClientsRepository clientsRepository;

  final Logger logger = LoggerFactory.getLogger(ServicesRepository.class);

  public Optional<List<Clients>> getAllClients() throws ServiceException {
    try {
      return clientsRepository.getAllClients();
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve all clients", e);
    }
  }

  public Optional<Clients> getClientById(String clientID) throws ServiceException {
    try {
      return clientsRepository.getClientsById(clientID);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve all clients", e);
    }
  }

  public Clients postClients(Clients client) throws ServiceException {
    try {
      return clientsRepository.postClients(client);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to post client", e);
    }
  }

  public Clients updateClient(Clients client) throws ServiceException {
    try {
      return clientsRepository.updateClients(client);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to update client", e);
    }
  }

  public int deleteById(String clientID) throws ServiceException {
    try {
      return clientsRepository.deleteById(clientID);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to delete client", e);
    }
  }

}
