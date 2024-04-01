package com.rkfcheung.portfolio.source;

import com.rkfcheung.portfolio.model.Position;

import java.util.List;

public interface Source {

    boolean available();

    List<Position> load();
}
