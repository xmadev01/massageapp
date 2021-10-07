package com.xms.app.massage.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SingleTreatmentVO {

    private long treatmentId;
    private String customerName;
    @DateTimeFormat(pattern="dd/MM/yyyy")
    private LocalDate serviceDate;
    private String practitionerId;
    private String itemName;
    private Integer duration;
    private BigDecimal expenseAmt;
    private BigDecimal claimedAmt;
    private String medicalCaseRecord;
}
