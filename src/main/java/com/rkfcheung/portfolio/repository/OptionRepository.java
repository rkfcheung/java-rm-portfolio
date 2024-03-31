package com.rkfcheung.portfolio.repository;

import com.rkfcheung.portfolio.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OptionRepository extends JpaRepository<Option, UUID> {

    @Override
    @NonNull
    List<Option> findAll();

    @Override
    @NonNull
    Optional<Option> findById(final @NonNull UUID uuid);

    @Override
    @NonNull
    <S extends Option> S save(final @NonNull S entity);

    @Override
    @NonNull
    <S extends Option> List<S> saveAll(final @NonNull Iterable<S> entities);
}
