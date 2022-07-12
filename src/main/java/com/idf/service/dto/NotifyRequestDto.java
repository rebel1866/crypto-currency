package com.idf.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotifyRequestDto {
    private String userName;
    private String symbol;
    private double price;
}
