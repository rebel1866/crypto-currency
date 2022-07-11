package com.idf.dao.dataaccess;

import com.idf.dao.entity.Currency;
import org.springframework.data.repository.CrudRepository;

public interface DatabaseCurrencyDao extends CrudRepository<Currency, Integer> {
}
