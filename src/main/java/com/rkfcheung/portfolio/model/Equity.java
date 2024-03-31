package com.rkfcheung.portfolio.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Builder
@Entity
public class Equity implements Security {

    @Id
    private UUID id;

    private String symbol;
}
