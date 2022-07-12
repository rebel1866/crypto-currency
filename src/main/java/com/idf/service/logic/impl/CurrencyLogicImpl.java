package com.idf.service.logic.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idf.dao.dbaccess.CurrencyDao;
import com.idf.dao.dbaccess.NotifyDao;
import com.idf.dao.entity.Currency;
import com.idf.dao.entity.NotifyRequest;
import com.idf.service.dto.CurrencyDto;
import com.idf.service.dto.NotifyRequestDto;
import com.idf.service.dtoconverter.CurrencyEntityToDtoConverter;
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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class CurrencyLogicImpl implements CurrencyLogic {

    private CurrencyDao currencyDao;
    private NotifyDao notifyDao;


    @Autowired
    public void setNotifyDao(NotifyDao notifyDao) {
        this.notifyDao = notifyDao;
    }

    @Autowired
    public void setCurrencyDao(CurrencyDao currencyDao) {
        this.currencyDao = currencyDao;
    }

    @Override
    public List<CurrencyDto> findAllCurrencies() {
        List<Currency> currenciesEntity = currencyDao.findAll();
        return CurrencyEntityToDtoConverter.convertList(currenciesEntity);
    }

    @Override
    public CurrencyDto findCurrencyById(int id) {
        Currency currency = currencyDao.findById(id).get();
        return CurrencyEntityToDtoConverter.convert(currency);
    }

    @Transactional
    public Currency updateCurrencyPriceById(int id, double price) {
        Currency currency = currencyDao.findById(id).get();
        currency.setPrice(price);
        return currency;
    }

    @Override
    @Transactional
    public List<CurrencyDto> updateCurrencyPrices() {
        List<Currency> currencies = currencyDao.findAll();
        List<Currency> updatedCurrencies = currencies.stream().map(value -> {
            try {
                return updateCurrencyPriceById(value.getId(), getRelevantPriceById(value.getId()));
            } catch (Exception e) {
                System.out.println("caught");
                return new Currency();
            }
        }).toList();
        return CurrencyEntityToDtoConverter.convertList(updatedCurrencies);
    }

    public double getRelevantPriceById(int id) throws Exception {
        String json;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet("https://api.coinlore.net/api/ticker/?id=" + id);
            json = client.execute(request, httpResponse -> EntityUtils.toString(httpResponse.getEntity()));
            if (json.equals("")) {
                throw new Exception();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonNode parent;
        try {
            parent = new ObjectMapper().readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return parent.get(0).get("price_usd").asDouble();
    }

    @Override
    public NotifyRequestDto handleNotifyRequest(NotifyRequestDto notifyRequestDto) {
        Currency currency = currencyDao.findCurrencyBySymbol(notifyRequestDto.getSymbol());
        double initialPrice = currency.getPrice();
        notifyRequestDto.setPrice(initialPrice);
        NotifyRequest notifyRequest = NotifyDtoToEntityConverter.convert(notifyRequestDto);
        notifyRequest.setCurrency(currency);
        NotifyRequest notifyRequestAdded = notifyDao.save(notifyRequest);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            while (true) {
                TimeUnit.SECONDS.sleep(12);
                checkPriceChange(notifyRequestAdded);
            }
        });
        return NotifyEntityToDtoConverter.convert(notifyRequestAdded);
    }

    private void checkPriceChange(NotifyRequest notifyRequest) {
        double initialPrice = notifyRequest.getPrice();
        int currencyId = notifyRequest.getCurrency().getId();
        double currentPrice = currencyDao.findById(currencyId).orElseThrow(() -> new RuntimeException("as")).getPrice();
        System.out.println("initial: " + initialPrice);
        System.out.println("current (db): " + currentPrice);
        System.out.println("symbol: " + notifyRequest.getCurrency().getSymbol());
        System.out.println("user name: " + notifyRequest.getUser().getUserName());
    }
}
