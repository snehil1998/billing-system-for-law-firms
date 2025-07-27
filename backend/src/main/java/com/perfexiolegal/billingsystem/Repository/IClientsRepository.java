package com.perfexiolegal.billingsystem.Repository;

import java.util.Optional;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Model.ClientDetails;

public interface IClientsRepository extends IRepository<ClientDetails> { 
    Optional<ClientDetails> getById(String id) throws RepositoryException;
    int deleteById(String id) throws RepositoryException;
}
