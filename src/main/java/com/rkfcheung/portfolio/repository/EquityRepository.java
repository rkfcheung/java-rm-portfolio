package com.rkfcheung.portfolio.repository;

import com.rkfcheung.portfolio.model.Equity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EquityRepository extends JpaRepository<Equity, UUID> {

    @Override
    @NonNull
    List<Equity> findAll();

    @Override
    @NonNull
    Optional<Equity> findById(final @NonNull UUID uuid);

    @Override
    @NonNull
    <S extends Equity> S save(final @NonNull S entity);

    @Override
    @NonNull
    <S extends Equity> List<S> saveAll(final @NonNull Iterable<S> entities);
}
