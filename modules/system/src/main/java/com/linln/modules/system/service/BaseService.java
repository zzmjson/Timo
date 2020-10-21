package com.linln.modules.system.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BaseService<T> {
    public Page<T> fetchDataBySearch(String searchText, Pageable pageable);
    public List<T> findAll();
    public T fetchOne(String id);
    public void update(T t);
    public void deleteById(String id);
    public void save(T t);
}
