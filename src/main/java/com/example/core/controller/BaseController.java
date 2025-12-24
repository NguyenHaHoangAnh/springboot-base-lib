package com.example.core.controller;

import com.example.core.constant.ResultCode;
import com.example.core.message.ResponseMsg;
import com.example.core.repository.BaseRepo;
import com.example.core.service.CRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseController<Bean, ID, Repository extends BaseRepo<Bean, ID>, Service extends CRUDService<Bean, ID, Repository>> {
    protected static int MAX_PAGE_RESULT = 100000;
    @Autowired
    private Service service;

    public Service getService() {
        return service;
    }

    public abstract void merge(Bean newBean, Bean currentBean) throws Exception;

    protected Pageable fixPageable(Pageable pageable) {
        return (Pageable) pageable != null ? pageable : PageRequest.of(0, MAX_PAGE_RESULT);
    }

    public ResponseMsg<?> findAll() throws Exception {
        List<Bean> list = this.getService().findAll();
        return ResponseMsg.newOKResponse(list);
    }

    public ResponseMsg<?> findAll(Pageable pageable) throws Exception {
        pageable = this.fixPageable(pageable);
        Page<Bean> page = this.getService().findAll(pageable);
        List<Bean> list = new ArrayList<>(page.getContent());
        Page<Bean> result = new PageImpl<>(list, pageable, page.getTotalElements());
        return ResponseMsg.newOKResponse(result);
    }

    public ResponseMsg<?> findAll(Specification<Bean> specification) throws Exception {
        List<Bean> list = this.getService().findAll(specification);
        return ResponseMsg.newOKResponse(list);
    }

    public ResponseMsg<?> findAll(Specification<Bean> specification, Pageable pageable) throws Exception {
        pageable = this.fixPageable(pageable);
        Page<Bean> page = this.getService().findAll(specification, pageable);
        List<Bean> list = new ArrayList<>(page.getContent());
        Page<Bean> result = new PageImpl<>(list, pageable, page.getTotalElements());
        return ResponseMsg.newOKResponse(result);
    }

    public ResponseMsg<?> findById(ID id) throws Exception {
        Bean entity = (Bean) this.getService().findById(id);
        return ResponseMsg.newOKResponse(entity);
    }

    public ResponseMsg<?> create(Bean bean) throws Exception {
        if (this.getService().saveBean(bean)) {
            return ResponseMsg.newOKResponse(bean);
        }
        return ResponseMsg.new500ErrorResponse();
    }

    public ResponseMsg<?> createAll(List<Bean> beans) throws Exception {
        if (this.getService().saveAllBeans(beans)) {
            return ResponseMsg.newOKResponse(beans);
        }
        return ResponseMsg.new500ErrorResponse();
    }

    public ResponseMsg<?> update(ID id, Bean bean) throws Exception {
        if (id != null && this.getService().findById(id) != null) {
            Bean currentBean = (Bean) this.getService().findById(id);
            this.merge(bean, currentBean);
            return this.getService().saveBean(bean) ? ResponseMsg.newOKResponse(currentBean): ResponseMsg.new500ErrorResponse();
        }
        return ResponseMsg.newResponse(ResultCode.OBJECT_EXISTED);
    }

    public ResponseMsg<?> delete(ID id) throws Exception {
        if (id != null && this.getService().findById(id) != null) {
            return this.getService().deleteById(id) ? ResponseMsg.newOKResponse() : ResponseMsg.new500ErrorResponse();
        }
        return ResponseMsg.newResponse(ResultCode.OBJECT_NOT_EXIST);
    }
}
