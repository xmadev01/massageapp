package com.xms.app.massage.enums;

public enum ServiceTypeEnum {
    HEAD("Head"),
    FOOT("Foot"),
    FULL_BODY("Full Body"),
    ACUPUNCTURE("Acupuncture");

    private String displayName;
    ServiceTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
