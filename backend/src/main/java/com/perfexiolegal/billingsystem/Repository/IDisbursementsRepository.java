package com.perfexiolegal.billingsystem.Repository;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Model.DisbursementDetails;

import java.util.List;
import java.util.Optional;

public interface IDisbursementsRepository extends IRepository<DisbursementDetails> {
    Optional<List<DisbursementDetails>> getByClientId(String clientID) throws RepositoryException;
    Optional<List<DisbursementDetails>> getByCaseId(String caseID) throws RepositoryException;
}