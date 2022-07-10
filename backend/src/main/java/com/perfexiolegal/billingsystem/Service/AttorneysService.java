package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.Attorneys;
import com.perfexiolegal.billingsystem.Model.Clients;
import com.perfexiolegal.billingsystem.Repository.AttorneysRepository;
import com.perfexiolegal.billingsystem.Repository.ClientsRepository;
import com.perfexiolegal.billingsystem.Repository.ServicesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Service
public class AttorneysService {

  @Autowired
  AttorneysRepository attorneysRepository;

  final Logger logger = LoggerFactory.getLogger(AttorneysService.class);

  public Optional<List<Attorneys>> getAllAttorneys() throws ServiceException {
    try {
      return attorneysRepository.getAllAttorneys();
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve all attorneys", e);
    }
  }

  public Optional<Attorneys> getAttorneyById(UUID attorneyID) throws ServiceException {
    try {
      return attorneysRepository.getAttorneyById(attorneyID);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to retrieve attorney", e);
    }
  }

  public Attorneys postAttorneys(Attorneys attorney) throws ServiceException {
    try {
      return attorneysRepository.postAttorneys(attorney);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to post attorney", e);
    }
  }

  public Attorneys updateAttorney(Attorneys attorney) throws ServiceException {
    try {
      return attorneysRepository.updateAttorneys(attorney);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to update attorney", e);
    }
  }

  public int deleteById(UUID attorneyID) throws ServiceException {
    try {
      return attorneysRepository.deleteById(attorneyID);
    } catch (RepositoryException e) {
      throw new ServiceException("unable to delete attorney", e);
    }
  }

}
