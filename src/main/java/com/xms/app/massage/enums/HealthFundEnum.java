package com.xms.app.massage.enums;

public enum HealthFundEnum {
    MEDIBANK("Medibank"),
    BUPA("Bupa"),
    HCF("HCF"),
    NIB("Nib"),
    HBF("HBF"),
    AUSTRALIAN_UNITY("Australian Unity"),
    TEACHERS_HEALTH("Teachers Health"),
    GBHBA("GBHBA"),
    DEFENCE_HEALTH("Defence Health"),
    CBHS("CBHS");

    private String displayName;

    HealthFundEnum(String displayName) {
        this.displayName = displayName;
    }
}
