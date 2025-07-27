package com.perfexiolegal.billingsystem.Repository;

import java.util.Optional;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Model.CaseDetails;

public interface ICasesRepository extends IRepository<CaseDetails> { 
    Optional<CaseDetails> getById(String id) throws RepositoryException;
    int deleteById(String id) throws RepositoryException;
}
