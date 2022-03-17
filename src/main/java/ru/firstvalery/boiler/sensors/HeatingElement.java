package ru.firstvalery.boiler.sensors;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HeatingElement {
    private String name;
    private State state;
    private boolean voltage;
    private boolean contactor;
    private boolean start;
    private boolean stop;

    public enum State {
        ON,
        OFF,
        FAIL
    }

    public boolean isOn() {
        return State.ON.equals(state);
    }
}
