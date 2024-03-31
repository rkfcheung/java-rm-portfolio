package com.rkfcheung.portfolio.source;

import com.rkfcheung.portfolio.model.PortfolioPosition;

import java.util.List;

public interface Source {

    boolean available();

    List<PortfolioPosition> load();
}
