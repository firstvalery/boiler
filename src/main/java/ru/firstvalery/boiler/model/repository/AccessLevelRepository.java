package ru.firstvalery.boiler.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.firstvalery.boiler.model.entity.AccessLevel;

import java.util.Optional;

public interface AccessLevelRepository extends JpaRepository<AccessLevel, Integer> {

    @Transactional
    Optional<AccessLevel> findByCode(String code);
}
