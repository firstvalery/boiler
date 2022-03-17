package ru.firstvalery.boiler.sensors;

import java.util.List;

public interface SensorService {
    List<AnalogParameter> getAnalogParameters();

    List<HeatingElement> getHeatingElements();
}
