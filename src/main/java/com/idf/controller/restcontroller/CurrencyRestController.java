package com.idf.controller.restcontroller;

import com.idf.service.dto.CurrencyDto;
import com.idf.service.dto.NotifyRequestDto;
import com.idf.service.logic.CurrencyLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

/**
 * This class encapsulates logic for handling http requests and generate responses using rest
 * @author Stanislav Melnikov
 * @version 1.0
 */

@RestController
public class CurrencyRestController {
    private CurrencyLogic currencyLogic;

    @Autowired
    public void setCurrencyLogic(CurrencyLogic currencyLogic) {
        this.currencyLogic = currencyLogic;
    }

    @GetMapping(value = "/currencies", produces = {"application/json"})
    public List<CurrencyDto> getCurrencies() {
        List<CurrencyDto> currencyDtoList = currencyLogic.findAllCurrencies();
        currencyDtoList.forEach((currencyDto) -> {
            Link priceLink = linkTo(methodOn(CurrencyRestController.class).getCurrencyById(currencyDto.getId())).withRel("price");
            currencyDto.add(priceLink);
        });
        return currencyDtoList;
    }

    @GetMapping(value = "/currencies/{id}", produces = {"application/json"})
    public CurrencyDto getCurrencyById(@PathVariable("id") int id) {
        return currencyLogic.findCurrencyById(id);
    }

    @PostMapping(value = "/notify")
    public NotifyRequestDto notifyMe(@RequestBody @Valid NotifyRequestDto notifyRequestDto) {
        return currencyLogic.handleNotifyRequest(notifyRequestDto);
    }
}
