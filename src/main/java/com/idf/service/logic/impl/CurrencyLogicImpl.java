package com.idf.service.logic.impl;

import com.idf.dao.dataaccess.DatabaseCurrencyDao;
import com.idf.dao.dataaccess.JsonCurrencyDao;
import com.idf.dao.entity.Currency;
import com.idf.service.dto.CurrencyDto;
import com.idf.service.dto.CurrencyPriceDto;
import com.idf.service.dtoconverter.CurrencyEntityToDtoConverter;
import com.idf.service.dtoconverter.CurrencyEntityToPriceDtoConverter;
import com.idf.service.logic.CurrencyLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
