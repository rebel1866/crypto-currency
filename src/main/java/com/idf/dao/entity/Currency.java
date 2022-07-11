package com.idf.dao.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prices")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String symbol;
    private double price;
}
