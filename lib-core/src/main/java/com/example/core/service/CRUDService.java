package com.example.core.service;

import com.example.core.repository.BaseRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CRUDService<Bean, ID, Repository extends BaseRepo<Bean, ID>> {
    List<Bean> findAll() throws Exception;

    Page<Bean> findAll(Specification<Bean> specification, Pageable pageable) throws Exception;

    Page<Bean> findAll(Pageable pageable) throws Exception;

    List<Bean> findAll(Specification<Bean> specification) throws Exception;

    Bean findById(ID id) throws Exception;

    boolean saveBean(Bean bean) throws Exception;

    boolean saveAllBeans(List<Bean> beans) throws Exception;

    boolean deleteById(ID id) throws Exception;
}
