package com.rkfcheung.portfolio.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.MILLIS;

@Getter
@RequiredArgsConstructor
public class ExpireAfter<T> {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final T data;
    private final long duration;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(timestamp.plus(duration, MILLIS));
    }
}
