package ru.firstvalery.boiler.sensors;

import ru.firstvalery.boiler.init.Initializable;

import java.util.List;

public interface SensorService extends Initializable {
    List<AnalogSensor> getAnalogParameters();

    List<HeatingElement> getHeatingElements();
}
