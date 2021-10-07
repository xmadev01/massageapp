package com.xms.app.massage.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class TreatmentVO {

    private String customerName;
    private LocalDate serviceDate;
    private String practitionerId;
    private List<Long> itemIds;
    private String otherItemName;
    private BigDecimal expenseAmt;
    private BigDecimal claimedAmt;
    private String medicalCaseRecord;
}
