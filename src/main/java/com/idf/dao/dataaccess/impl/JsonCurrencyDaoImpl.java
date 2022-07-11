package com.idf.dao.dataaccess.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idf.dao.dataaccess.JsonCurrencyDao;
import com.idf.dao.entity.Currency;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Repository
public class JsonCurrencyDaoImpl implements JsonCurrencyDao {
    @Override
    public List<Currency> findAllCurrencies() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Currency> currencyList = null;
        try {
            currencyList = objectMapper.readValue(new File("target/classes/currency-list.json"),  //prsf
                    new TypeReference<>() {
                    });
        } catch (IOException e) {
            System.out.println(e);
        }
        return currencyList;
    }
}
