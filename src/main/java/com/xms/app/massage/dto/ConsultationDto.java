package com.xms.app.massage.dto;

import com.xms.app.massage.model.Item;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConsultationDto {

    private String serviceDate;
    private String customerName;
    private Item item;
    private String type;
    private String healthFund;
    private BigDecimal paidAmt;
    private BigDecimal claimedAmt;
}
