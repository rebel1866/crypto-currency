package com.idf.service.dtoconverter;

import com.idf.dao.entity.Currency;
import com.idf.service.dto.CurrencyPriceDto;

import java.util.List;

public class CurrencyEntityToPriceDtoConverter {
    public static CurrencyPriceDto convert(Currency currency){
        CurrencyPriceDto currencyPriceDto = new CurrencyPriceDto();
        currencyPriceDto.setId(currency.getId());
        currencyPriceDto.setPrice(currency.getPrice());
        return currencyPriceDto;
    }
    public static List<CurrencyPriceDto> convertList(List<Currency> currencies){
        return currencies.stream().map(CurrencyEntityToPriceDtoConverter::convert).toList();
    }
}
