package com.example.core.service;

import com.example.core.repository.BaseRepo;

public interface CRUDService<Bean, ID, Repository extends BaseRepo<Bean, ID>> {
}
