package com.idf.dao.dataaccess;

import com.idf.dao.entity.NotifyRequest;
import org.springframework.data.repository.CrudRepository;

public interface NotifyDao extends CrudRepository<NotifyRequest, Integer> {
}
