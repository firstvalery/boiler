package ru.firstvalery.boiler.sensors;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AnalogParameter {
    int offset;
    double value;
    String name;
    double minLim;
    double maxLim;
    double minAlarm;
    double maxAlarm;
    double minUn;
    double maxUn;
    State state;

    static enum State {
        MIN_ALARM,
        MIN_LIM,
        NORMAL,
        MAX_LIM,
        MAX_ALARM,
        UNDEFINED
    }

    void recalculate(short[] readArea) {
        short rowInt = readArea[offset];
        this.value = rowInt / 100.0;
    }
}
