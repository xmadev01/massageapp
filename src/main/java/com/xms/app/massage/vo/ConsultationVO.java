package com.xms.app.massage.vo;

import com.xms.app.massage.model.Item;
import lombok.Data;

@Data
public class ConsultationVO {

    private long treatmentId;
    private String serviceDate;
    private String customerName;
    private Item item;
    private String type;
    private Integer duration;
    private String healthFund;
    private String paidAmt;
    private String claimedAmt;
    private String medicalCaseRecord;
}
