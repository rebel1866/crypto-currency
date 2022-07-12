package com.idf.dao.dbaccess;

import com.idf.dao.entity.NotifyRequest;
import org.springframework.data.repository.CrudRepository;

/**
 * This interface provides database access for NotifyRequest entities
 *
 * @author Stanislav Melnikov
 * @version 1.0
 */
public interface NotifyDao extends CrudRepository<NotifyRequest, Integer> {
}
