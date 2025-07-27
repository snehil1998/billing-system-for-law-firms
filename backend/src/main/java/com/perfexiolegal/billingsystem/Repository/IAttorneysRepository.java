package com.perfexiolegal.billingsystem.Repository;

import java.util.Optional;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Model.AttorneyDetails;

public interface IAttorneysRepository extends IRepository<AttorneyDetails> { 
    Optional<AttorneyDetails> getById(String id) throws RepositoryException;
    int deleteById(String id) throws RepositoryException;
}
