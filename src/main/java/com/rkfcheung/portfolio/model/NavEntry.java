package com.rkfcheung.portfolio.model;

import com.rkfcheung.portfolio.util.ValueUtil;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Data
public class NavEntry {

    private final Security security;
    private BigDecimal price;
    private final Long qty;
    private BigDecimal value;


    public NavEntry(final @NonNull Position position, final BigDecimal price) {
        this.security = position.getSecurity();
        this.price = price;
        this.qty = position.getQty();
        this.update(price);
    }

    public void update(final BigDecimal price) {
        this.price = price;
        this.value = ValueUtil.round(this.price.multiply(BigDecimal.valueOf(this.qty)));
    }
}
