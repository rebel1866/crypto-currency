package com.idf.service.logic.impl;

import com.idf.ServiceLayerTest;
import com.idf.dao.dbaccess.CurrencyDao;
import com.idf.dao.entity.Currency;
import com.idf.service.dto.CurrencyDto;
import com.idf.service.dtoconverter.CurrencyEntityToDtoConverter;
import com.idf.service.exception.ServiceException;
import com.idf.service.logic.CurrencyLogic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ServiceLayerTest.class)
class CurrencyLogicImplTest {

    private CurrencyLogic currencyLogic;

    @MockBean
    private CurrencyDao currencyDao;

    @Autowired
    public void setCurrencyLogic(CurrencyLogic currencyLogic) {
        this.currencyLogic = currencyLogic;
    }

    @BeforeEach
    public void before() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void findAllCurrencies() {
        List<Currency> currencies = new ArrayList<>();
        Currency currency = new Currency(1, "BTC", 2.0);
        currencies.add(currency);
        Mockito.when(currencyDao.findAll()).thenReturn(currencies);
        List<CurrencyDto> currencyDtoList = currencyLogic.findAllCurrencies();
        Assertions.assertEquals("BTC", currencyDtoList.get(0).getSymbol());
    }

    @Test
    void findCurrencyById() {
        Currency currency = new Currency(1, "SOL", 2.0);
        Mockito.when(currencyDao.findById(1)).thenReturn(Optional.of(currency));
        CurrencyDto currencyDto = currencyLogic.findCurrencyById(1);
        Assertions.assertEquals("SOL", currencyDto.getSymbol());
    }

    @Test
    void updateCurrencyPriceById() {
        Currency currency = new Currency(1, "SOL", 2.0);
        Mockito.when(currencyDao.findById(1)).thenReturn(Optional.of(currency));
        Assertions.assertEquals(3.0, currencyLogic.updateCurrencyPriceById(1, 3.0).getPrice());
    }

    @Test
    void updateCurrencyPrices() {
        Currency currency = new Currency(1, "SOL", 2.0);
        List<Currency> currencies = new ArrayList<>();
        currencies.add(currency);
        Mockito.when(currencyDao.findAll()).thenReturn(currencies);
        CurrencyLogic mock = Mockito.mock(CurrencyLogic.class);
        Mockito.when(mock.getRelevantPriceById(1)).thenReturn(3.0);
        Mockito.when(mock.updateCurrencyPriceById(1, 3.0)).thenReturn(currency);
        List<CurrencyDto> currencyDtoList = currencyLogic.updateCurrencyPrices();
        Assertions.assertEquals(2.0, currencyDtoList.get(0).getPrice());
    }
}