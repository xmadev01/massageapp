package com.xms.app.massage.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SingleTreatmentVO {

    private long treatmentId;
    private String customerName;
    private LocalDate serviceDate;
    private String practitionerId;
    private String itemName;
    private String medicalCaseRecord;
}
