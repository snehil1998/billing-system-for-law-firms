package com.perfexiolegal.billingsystem.Repository;

import com.perfexiolegal.billingsystem.Exceptions.RepositoryException;

import java.util.List;
import java.util.Optional;

public interface IRepository<T> {
    Optional<List<T>> getAll() throws RepositoryException;
    Optional<T> getById(String id) throws RepositoryException;
    void create(T entity) throws RepositoryException;
    void update(T entity) throws RepositoryException;
    int deleteById(String id) throws RepositoryException;
} 