package com.rkfcheung.portfolio.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
public class Option implements Security {

    @Id
    private UUID id;

    private String symbol;

    private String underlying;

    private BigDecimal strike;

    private Date maturity;

    private OptionType type;
}
