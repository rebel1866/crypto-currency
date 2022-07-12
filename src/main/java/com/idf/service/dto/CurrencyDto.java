package com.idf.service.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class CurrencyDto extends RepresentationModel<CurrencyDto> {
    private int id;
    private String symbol;
    private double price;
}
