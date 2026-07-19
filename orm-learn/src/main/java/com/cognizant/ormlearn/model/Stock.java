package com.cognizant.ormlearn.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "st_id")
    private int id;

    @Column(name = "st_code", length = 10, nullable = false)
    private String code;

    @Column(name = "st_date", nullable = false)
    private Date date;

    @Column(name = "st_open", precision = 10, scale = 2, nullable = false)
    private BigDecimal open;

    @Column(name = "st_close", precision = 10, scale = 2, nullable = false)
    private BigDecimal close;

    @Column(name = "st_volume", nullable = false)
    private long volume;
}
