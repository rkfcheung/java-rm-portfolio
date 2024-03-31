package com.rkfcheung.portfolio.repository;

import com.rkfcheung.portfolio.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OptionRepository extends JpaRepository<Option, UUID> {
}
