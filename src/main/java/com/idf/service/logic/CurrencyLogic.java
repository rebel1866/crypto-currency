package com.idf.service.logic;

import com.idf.service.dto.CurrencyDto;
import com.idf.service.dto.CurrencyPriceDto;
import com.idf.service.dto.NotifyRequestDto;

import java.util.List;

public interface CurrencyLogic {
    List<CurrencyDto> findAllCurrencies();

    CurrencyPriceDto findCurrencyById(int id);

    List<CurrencyPriceDto> updateCurrencyPrices();

    NotifyRequestDto handleNotifyRequest(NotifyRequestDto notifyRequestDto);
}
