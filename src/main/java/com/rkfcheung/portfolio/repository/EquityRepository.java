package com.rkfcheung.portfolio.repository;

import com.rkfcheung.portfolio.model.Equity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EquityRepository extends JpaRepository<Equity, UUID> {
}
