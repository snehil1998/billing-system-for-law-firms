package com.perfexiolegal.billingsystem.Service;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;

import java.util.List;
import java.util.Optional;

public interface IService<T> {
    Optional<List<T>> getAll() throws ServiceException;
    Optional<T> getById(String id) throws ServiceException;
    void create(T entity) throws ServiceException;
    void update(T entity) throws ServiceException;
    int deleteById(String id) throws ServiceException;
} 