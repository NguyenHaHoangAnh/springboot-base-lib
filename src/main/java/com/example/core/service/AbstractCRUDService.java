package com.example.core.service;

import com.example.core.repository.BaseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public abstract class AbstractCRUDService<Bean, ID, Repository extends BaseRepo<Bean, ID>> implements CRUDService<Bean, ID, Repository> {
    @Autowired
    private Repository repository;

    public List<Bean> findAll() throws Exception {
        return this.repository.findAll();
    }

    public Page<Bean> findAll(Specification<Bean> specification, Pageable pageable) throws Exception {
        return this.repository.findAll(specification, pageable);
    }

    public Page<Bean> findAll(Pageable pageable) throws Exception {
        return this.repository.findAll(pageable);
    }

    public List<Bean> findAll(Specification<Bean> specification) throws Exception {
        return this.repository.findAll(specification);
    }

    public Bean findById(ID id) throws Exception {
        return (Bean) this.repository.findById(id).get();
    }

    public boolean saveBean(Bean bean) throws Exception {
        this.repository.save(bean);
        return true;
    }

    public boolean saveAllBeans(List<Bean> beans) throws Exception {
        return this.repository.saveAll(beans).size() == beans.size();
    }

    public boolean deleteById(ID id) throws Exception {
        this.repository.deleteById(id);
        return true;
    }
}
