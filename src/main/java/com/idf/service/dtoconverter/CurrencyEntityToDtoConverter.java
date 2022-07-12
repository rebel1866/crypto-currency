package com.idf.service.dtoconverter;

import com.idf.dao.entity.Currency;
import com.idf.service.dto.CurrencyDto;

import java.util.List;
import java.util.stream.Collectors;

public class CurrencyEntityToDtoConverter {
    public static CurrencyDto convert(Currency currency) {
        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setId(currency.getId());
        currencyDto.setSymbol(currency.getSymbol());
        currencyDto.setPrice(currency.getPrice());
        return currencyDto;
    }

    public static List<CurrencyDto> convertList(List<Currency> currencies) {
        return currencies.stream().map(CurrencyEntityToDtoConverter::convert).collect(Collectors.toList());
    }
}

