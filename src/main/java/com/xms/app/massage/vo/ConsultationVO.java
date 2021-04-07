package com.xms.app.massage.vo;

import com.xms.app.massage.model.Item;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConsultationVO {

    private String serviceDate;
    private String customerName;
    private Item item;
    private String practitionerName;
    private BigDecimal paidAmt;
    private BigDecimal claimedAmt;
}
