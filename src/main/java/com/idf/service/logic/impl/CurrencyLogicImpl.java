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
import com.idf.service.exception.ServiceException;
import com.idf.service.logic.CurrencyLogic;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This class represents application logic and implements CurrencyLogic interface
 * See description of methods in CurrencyLogic interface
 *
 * @author Stanislav Melnikov
 * @version 1.0
 * @see CurrencyLogic
 */
@Service
public class CurrencyLogicImpl implements CurrencyLogic {

    private CurrencyDao currencyDao;
    private NotifyDao notifyDao;
    private static final String API_URL = "https://api.coinlore.net/api/ticker/?id=";
    private static final double PERCENT_THRESHOLD = 1.0;


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
        Currency currency = currencyDao.findById(id).
                orElseThrow(() -> new ServiceException("No currency found by given id: " + id));
        return CurrencyEntityToDtoConverter.convert(currency);
    }

    @Transactional
    @Override
    public Currency updateCurrencyPriceById(int id, double price) {
        Currency currency = currencyDao.findById(id).
                orElseThrow(() -> new ServiceException("Can't update currency by given id - nothing found with id: " + id));
        currency.setPrice(price);
        return currency;
    }

    @Override
    @Transactional
    public List<CurrencyDto> updateCurrencyPrices() {
        Logger logger = LogManager.getLogger(CurrencyLogicImpl.class);
        List<Currency> currencies = currencyDao.findAll();
        List<Currency> updatedCurrencies = currencies.stream().map(value -> {
            try {
                return updateCurrencyPriceById(value.getId(), getRelevantPriceById(value.getId()));
            } catch (ServiceException e) {
                logger.error(e.getMessage());
                return new Currency();
            }
        }).toList();
        return CurrencyEntityToDtoConverter.convertList(updatedCurrencies);
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
                TimeUnit.MINUTES.sleep(2);
                checkPriceChange(notifyRequestAdded);
            }
        });
        return NotifyEntityToDtoConverter.convert(notifyRequestAdded);
    }

    @Override
    public double getRelevantPriceById(int id) throws ServiceException {
        String json;
        JsonNode parent;
        String targetField = "price_usd";
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(API_URL + id);
            json = client.execute(request, httpResponse -> EntityUtils.toString(httpResponse.getEntity()));
            if (json.equals("")) {
                throw new ServiceException("Exception is caught - can not update currency using coinlore api");
            }
            parent = new ObjectMapper().readTree(json);
        } catch (JsonProcessingException e) {
            throw new ServiceException("Coinlore api response can not be parsed");
        } catch (IOException e) {
            throw new ServiceException("Exception while trying to make coinlore api http request");
        }
        JsonNode firstNode = parent.get(0);
        return firstNode.get(targetField).asDouble();
    }

    public void checkPriceChange(NotifyRequest notifyRequest) {
        Logger logger = LogManager.getLogger(CurrencyLogicImpl.class);
        int currencyId = notifyRequest.getCurrency().getId();
        double initialPrice = notifyRequest.getPrice();
        double currentPrice = currencyDao.findById(currencyId).
                orElseThrow(() -> new ServiceException("No currency found")).getPrice();
        String symbol = notifyRequest.getCurrency().getSymbol();
        String userName = notifyRequest.getUser().getUserName();
        double difference = initialPrice - currentPrice;
        double percents = (Math.abs(difference / initialPrice)) * 100;
        if (percents > PERCENT_THRESHOLD) {
            String message = String.format("Info for user %s - price for %s currency has changed for %s percents",
                    userName, symbol, percents);
            logger.warn(message);
        }
    }
}
