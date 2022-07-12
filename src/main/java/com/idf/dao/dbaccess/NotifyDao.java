package com.idf.dao.dbaccess;

import com.idf.dao.entity.NotifyRequest;
import org.springframework.data.repository.CrudRepository;

public interface NotifyDao extends CrudRepository<NotifyRequest, Integer> {
}
