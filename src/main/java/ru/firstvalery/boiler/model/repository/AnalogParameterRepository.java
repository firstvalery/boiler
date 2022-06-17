package ru.firstvalery.boiler.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.firstvalery.boiler.model.entity.AnalogParameter;

public interface AnalogParameterRepository extends JpaRepository<AnalogParameter, Integer> {
}
