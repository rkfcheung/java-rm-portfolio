package com.rkfcheung.portfolio.repository;

import com.rkfcheung.portfolio.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {

    @Override
    @NonNull
    List<Portfolio> findAll();

    @Override
    @NonNull
    <S extends Portfolio> S save(final @NonNull S entity);

    @Override
    @NonNull
    <S extends Portfolio> List<S> saveAll(final @NonNull Iterable<S> entities);
}
