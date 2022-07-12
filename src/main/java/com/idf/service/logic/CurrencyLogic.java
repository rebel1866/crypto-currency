package com.idf.service.logic;

import com.idf.service.dto.CurrencyDto;
import com.idf.service.dto.NotifyRequestDto;

import java.util.List;

public interface CurrencyLogic {
    List<CurrencyDto> findAllCurrencies();

    CurrencyDto findCurrencyById(int id);

    List<CurrencyDto> updateCurrencyPrices();

    NotifyRequestDto handleNotifyRequest(NotifyRequestDto notifyRequestDto);
}
