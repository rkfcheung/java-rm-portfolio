package com.rkfcheung.portfolio.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
public class Portfolio {

    @Id
    @Column(name = "symbol_id")
    private UUID symbolId;

    private Long qty;
}
