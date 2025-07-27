package com.perfexiolegal.billingsystem.Repository;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;
import com.perfexiolegal.billingsystem.Model.DisbursementRequest;
import com.perfexiolegal.billingsystem.Model.DisbursementResponse;
import com.perfexiolegal.billingsystem.Model.DisbursementRequestDto;

import java.util.List;
import java.util.Optional;

public interface IDisbursementsRepository {
    Optional<List<DisbursementResponse>> getByClientId(String clientID) throws RepositoryException;
    Optional<List<DisbursementResponse>> getByCaseId(String caseID) throws RepositoryException;
    Optional<DisbursementResponse> getById(Long id) throws RepositoryException;
    int deleteById(Long id) throws RepositoryException;
    Optional<List<DisbursementResponse>> getAll() throws RepositoryException;
    void create(DisbursementRequest entity) throws RepositoryException;
    void update(Long id, DisbursementRequestDto entity) throws RepositoryException;
}