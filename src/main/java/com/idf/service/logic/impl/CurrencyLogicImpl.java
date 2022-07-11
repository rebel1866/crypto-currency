package com.idf.service.logic.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idf.dao.dataaccess.DatabaseCurrencyDao;
import com.idf.dao.dataaccess.JsonCurrencyDao;
import com.idf.dao.entity.Currency;
import com.idf.service.dto.CurrencyDto;
import com.idf.service.dto.CurrencyPriceDto;
import com.idf.service.dtoconverter.CurrencyEntityToDtoConverter;
import com.idf.service.dtoconverter.CurrencyEntityToPriceDtoConverter;
import com.idf.service.logic.CurrencyLogic;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyLogicImpl implements CurrencyLogic {

    private JsonCurrencyDao jsonCurrencyDao;
    private DatabaseCurrencyDao databaseCurrencyDao;

    @Autowired
    public void setJsonCurrencyDao(JsonCurrencyDao jsonCurrencyDao) {
        this.jsonCurrencyDao = jsonCurrencyDao;
    }

    @Autowired
    public void setDatabaseCurrencyDao(DatabaseCurrencyDao databaseCurrencyDao) {
        this.databaseCurrencyDao = databaseCurrencyDao;
    }

    @Override
    public List<CurrencyDto> findAllCurrencies() {
        List<Currency> currenciesEntity = jsonCurrencyDao.findAllCurrencies();
        return CurrencyEntityToDtoConverter.convertList(currenciesEntity);
    }

    @Override
    public CurrencyPriceDto findCurrencyById(int id) {
        Currency currency = databaseCurrencyDao.findById(id).get();
        return CurrencyEntityToPriceDtoConverter.convert(currency);
    }


    public Currency updateCurrencyPriceById(int id, double price) {
        Currency currency = databaseCurrencyDao.findById(id).get();
        currency.setPrice(price);
        return currency;
    }

    @Override
    @Transactional
    public List<CurrencyPriceDto> updateCurrencyPrices() {
        List<Currency> currencies = jsonCurrencyDao.findAllCurrencies();
        List<Currency> updatedCurrencies = currencies.stream().map(value -> updateCurrencyPriceById(value.getId(),
                getRelevantPriceById(value.getId()))).toList();
        return CurrencyEntityToPriceDtoConverter.convertList(updatedCurrencies);
    }

    public double getRelevantPriceById(int id) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://api.coinlore.net/api/ticker/?id=" + id).build();
        String json;
        try {
            Response response = client.newCall(request).execute();
            json = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonNode parent = null;
        try {
            parent = new ObjectMapper().readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return parent.get(0).get("price_usd").asDouble();
    }
}
