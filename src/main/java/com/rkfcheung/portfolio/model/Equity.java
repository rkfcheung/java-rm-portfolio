package com.rkfcheung.portfolio.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
public class Equity implements Security {

    @Id
    private UUID id;

    private String symbol;
}
