package ru.firstvalery.boiler.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.firstvalery.boiler.model.entity.AccessLevel;

public interface AccessLevelRepository extends JpaRepository<AccessLevel, Integer> {
}
