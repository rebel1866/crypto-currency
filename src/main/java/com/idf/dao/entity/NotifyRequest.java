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
@Table(name = "notify_requests")
public class NotifyRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notify_id")
    private int notifyId;
    private String symbol;
    private double price;
    @Column(name = "username")
    private String userName;

}
