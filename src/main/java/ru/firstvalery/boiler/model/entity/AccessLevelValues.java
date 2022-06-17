package ru.firstvalery.boiler.model.entity;

public enum AccessLevelValues {
    ACCESS_DENIED,
    MONITORING_ONLY,
    FULL_CONTROL;

    public static AccessLevelValues of(AccessLevel accessLevel) {
        return AccessLevelValues.valueOf(accessLevel.getCode());
    }
}
