package com.idf.service.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class NotifyRequestDto {
    @NotBlank(message = "user name field must not be blank")
    private String userName;
    @NotBlank(message = "symbol field must not be blank")
    private String symbol;
    private double price;
}
