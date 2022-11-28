package com.revature.resproject.daos;

import java.util.List;

public interface CrudDAO<T> {
    void save(T obj);
    void delete(T obj);
    void update(T obj);
    T findById();
    List<T> findAll();
}
