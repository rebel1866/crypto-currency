package com.idf.dao.dataaccess;

import com.idf.dao.entity.Currency;

import java.util.List;

public interface JsonCurrencyDao {
    List<Currency> findAllCurrencies();
}
