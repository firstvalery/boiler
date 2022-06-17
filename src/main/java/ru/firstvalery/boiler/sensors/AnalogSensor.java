package ru.firstvalery.boiler.sensors;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

@Getter
@Setter
@Builder
public class AnalogSensor {
    int offset;
    double value;
    String name;
    double minLim;
    double maxLim;
    double minAlarm;
    double maxAlarm;
    double minUn;
    double maxUn;
    double hysteresis;
    double blur;
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
        short shortH = readArea[offset];
        short shortL = readArea[offset + 1];
        byte[] bytes = new byte[]{(byte) (shortH), (byte) (shortH >> 8), (byte) (shortL), (byte) (shortL >> 8),};
        this.value = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        recalculateState();
    }

    private void recalculateState() {
        if (Objects.isNull(state) || State.UNDEFINED.equals(state)) {
            calculateStartState();
        }
        if (State.MIN_ALARM.equals(state)) {
            if (value < minUn - minUn * blur/100.0) {
                state = State.UNDEFINED;
            } else if (value >= minAlarm + hysteresis) {
                state = State.MIN_LIM;
            }
        } else if (State.MIN_LIM.equals(state)) {
            if (value < minAlarm - hysteresis) {
                state = State.MIN_ALARM;
            } else if (value >= minLim + hysteresis) {
                state = State.NORMAL;
            }
        } else if (State.NORMAL.equals(state)) {
            if (value < minLim - hysteresis) {
                state = State.MIN_LIM;
            } else if (value >= maxLim + hysteresis) {
                state = State.MAX_LIM;
            }
        } else if (State.MAX_LIM.equals(state)) {
            if (value < maxLim - hysteresis) {
                state = State.NORMAL;
            } else if (value >= maxAlarm + hysteresis) {
                state = State.MAX_ALARM;
            }
        } else if (State.MAX_ALARM.equals(state)) {
            if (value < maxAlarm - hysteresis) {
                state = State.MAX_LIM;
            } else if (value >= maxUn + hysteresis) {
                state = State.UNDEFINED;
            }
        }
    }

    private void calculateStartState() {
        if (value < minUn - minUn * blur/100.0 || value >= maxUn + maxUn * blur/100.0) {
            state = State.UNDEFINED;
        } else if (value < minAlarm) {
            state = State.MIN_ALARM;
        } else if (value < minLim) {
            state = State.MIN_ALARM;
        } else if (value < maxLim) {
            state = State.NORMAL;
        } else if (value < maxAlarm) {
            state = State.MAX_LIM;
        } else {
            state = State.MAX_ALARM;
        }
    }

    @Override
    public String toString() {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return name + ": " + bd.doubleValue();
    }
}
