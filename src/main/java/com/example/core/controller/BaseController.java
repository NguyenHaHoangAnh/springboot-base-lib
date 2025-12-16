package com.example.core.controller;

import com.example.core.repository.BaseRepo;
import com.example.core.service.CRUDService;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController<Bean, ID, Repository extends BaseRepo<Bean, ID>, Service extends CRUDService<Bean, ID, Repository>> {
    @Autowired
    private Service service;
}
