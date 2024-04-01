package com.rkfcheung.portfolio.source;

import com.rkfcheung.portfolio.model.Position;

import java.util.Collections;
import java.util.List;

public class EmptySource implements Source {

    @Override
    public boolean available() {
        return false;
    }

    @Override
    public List<Position> load() {
        return Collections.emptyList();
    }
}
