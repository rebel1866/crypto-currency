package com.idf.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyDto {
    private int id;
    private String symbol;
    private double price;
}
