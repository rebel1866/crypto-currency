package com.idf.controller;

import com.idf.service.dto.CurrencyDto;
import com.idf.service.dto.CurrencyPriceDto;
import com.idf.service.logic.CurrencyLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/currencies")
public class CurrencyRestController {
    private CurrencyLogic currencyLogic;

    @Autowired
    public void setCurrencyLogic(CurrencyLogic currencyLogic) {
        this.currencyLogic = currencyLogic;
    }

    @GetMapping(produces = {"application/json"})
    public List<CurrencyDto> getCurrencies() {
        return currencyLogic.findAllCurrencies();
    }

    @GetMapping(value = "/{id}", produces = {"application/json"})
    public CurrencyPriceDto getCurrencyById(@PathVariable("id") int id) {
        return currencyLogic.findCurrencyById(id);
    }
}
