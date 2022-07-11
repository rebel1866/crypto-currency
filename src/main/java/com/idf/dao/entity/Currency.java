package com.idf.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Currency {
    private int id;
    private String symbol;
    private int price;
}
