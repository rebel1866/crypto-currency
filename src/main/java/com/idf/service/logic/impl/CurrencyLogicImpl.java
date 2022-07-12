package com.idf.service.logic.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idf.dao.dataaccess.DatabaseCurrencyDao;
import com.idf.dao.dataaccess.JsonCurrencyDao;
import com.idf.dao.dataaccess.NotifyDao;
import com.idf.dao.entity.Currency;
import com.idf.dao.entity.NotifyRequest;
import com.idf.service.dto.CurrencyDto;
import com.idf.service.dto.CurrencyPriceDto;
import com.idf.service.dto.NotifyRequestDto;
import com.idf.service.dtoconverter.CurrencyEntityToDtoConverter;
import com.idf.service.dtoconverter.CurrencyEntityToPriceDtoConverter;
import com.idf.service.dtoconverter.NotifyDtoToEntityConverter;
import com.idf.service.dtoconverter.NotifyEntityToDtoConverter;
import com.idf.service.logic.CurrencyLogic;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class CurrencyLogicImpl implements CurrencyLogic {

    private JsonCurrencyDao jsonCurrencyDao;
    private DatabaseCurrencyDao databaseCurrencyDao;
    private NotifyDao notifyDao;

    @Autowired
    public void setJsonCurrencyDao(JsonCurrencyDao jsonCurrencyDao) {
        this.jsonCurrencyDao = jsonCurrencyDao;
    }

    @Autowired
    public void setNotifyDao(NotifyDao notifyDao) {
        this.notifyDao = notifyDao;
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

    @Transactional
    public Currency updateCurrencyPriceById(int id, double price) {
        System.out.println("--");
        Currency currency = databaseCurrencyDao.findById(id).get();
        currency.setPrice(price);
        System.out.println("---");
        return currency;
    }

    @Override
    @Transactional
    public List<CurrencyPriceDto> updateCurrencyPrices() {
        List<Currency> currencies = jsonCurrencyDao.findAllCurrencies();
//        List<Currency> updatedCurrencies = currencies.stream().map(value -> updateCurrencyPriceById(value.getId(),
//                getRelevantPriceById(value.getId()))).toList();
        List<Currency> updatedCurrencies = new ArrayList<>();
        for (Currency currency : currencies) {
            System.out.println("eee");
            Currency currency1;
            try {
                currency1 = updateCurrencyPriceById(currency.getId(), getRelevantPriceById(currency.getId()));
                updatedCurrencies.add(currency1);
            } catch (Exception e) {
                System.out.println("cauht-----------------");
            }
            System.out.println("qqq");

            System.out.println("bbb");
        }
        return CurrencyEntityToPriceDtoConverter.convertList(updatedCurrencies);
    }

    public double getRelevantPriceById(int id) throws Exception {
        String json;
        System.out.println(1);
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            System.out.println(2);
            HttpGet request = new HttpGet("https://api.coinlore.net/api/ticker/?id=" + id);
            System.out.println(3);
            json = client.execute(request, httpResponse -> EntityUtils.toString(httpResponse.getEntity()));
            if (json.equals("")) {
                throw new Exception();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonNode parent = null;
        System.out.println(5);
        try {
            parent = new ObjectMapper().readTree(json);
            System.out.println(6);
        } catch (JsonProcessingException e) {
            System.out.println(42428);
            throw new RuntimeException(e);
        }
        System.out.println(7);
        System.out.println("|" + json + "|");
        return parent.get(0).get("price_usd").asDouble();
    }

    @Override
    public NotifyRequestDto handleNotifyRequest(NotifyRequestDto notifyRequestDto) {
        int id = jsonCurrencyDao.findCurrencyIdBySymbol(notifyRequestDto.getSymbol());
        double currentPrice = databaseCurrencyDao.findById(id).get().getPrice();
        notifyRequestDto.setPrice(currentPrice);
        NotifyRequest notifyRequest = NotifyDtoToEntityConverter.convert(notifyRequestDto);
        NotifyRequest notifyRequestAdded = notifyDao.save(notifyRequest);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            while (true) {
                TimeUnit.SECONDS.sleep(12);
                checkPriceChange(notifyRequestDto, id);
            }
        });
        return NotifyEntityToDtoConverter.convert(notifyRequestAdded);
    }

    private void checkPriceChange(NotifyRequestDto notifyRequestDto, int id) {
        System.out.println(notifyRequestDto.getPrice());
        System.out.println(id);
        CurrencyPriceDto currencyPriceDto = findCurrencyById(id);
        double currentPrice = currencyPriceDto.getPrice();
        System.out.println(currentPrice);
        System.out.println(notifyRequestDto.getSymbol());
        System.out.println(notifyRequestDto.getUserName());
    }
}
