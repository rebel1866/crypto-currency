package com.idf.controller;

import com.idf.service.dto.CurrencyDto;
import com.idf.service.dto.CurrencyPriceDto;
import com.idf.service.dto.NotifyRequestDto;
import com.idf.service.logic.CurrencyLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CurrencyRestController {
    private CurrencyLogic currencyLogic;

    @Autowired
    public void setCurrencyLogic(CurrencyLogic currencyLogic) {
        this.currencyLogic = currencyLogic;
    }

    @GetMapping(value = "/currencies", produces = {"application/json"})
    public List<CurrencyDto> getCurrencies() {
        return currencyLogic.findAllCurrencies();
    }

    @GetMapping(value = "/currencies/{id}", produces = {"application/json"})
    public CurrencyPriceDto getCurrencyById(@PathVariable("id") int id) {
        return currencyLogic.findCurrencyById(id);
    }

    @PostMapping(value = "/notify")
    public NotifyRequestDto notifyMe(@RequestBody NotifyRequestDto notifyRequestDto) {
        return currencyLogic.handleNotifyRequest(notifyRequestDto);
    }
}
