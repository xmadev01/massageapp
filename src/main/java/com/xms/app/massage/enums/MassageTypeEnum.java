package com.xms.app.massage.enums;

public enum MassageTypeEnum {
    ACCUPUNCTURE("Accupuncture"),
    NATURAL_THERAPY("Natural Therapy");

    private String displayName;
    MassageTypeEnum(String displayName) {
        this.displayName = displayName;
    }
}
