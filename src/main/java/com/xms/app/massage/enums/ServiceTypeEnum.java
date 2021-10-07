package com.xms.app.massage.enums;

public enum ServiceTypeEnum {

    MASSAGE("Massage"),
    ACUPUNCTURE("Acupuncture"),
    CONSULTATION("Consultation"),
    OTHER("Other");

    private String displayName;
    ServiceTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
