package com.xms.app.massage.vo;

import com.xms.app.massage.model.Item;
import lombok.Data;

@Data
public class ConsultationVO {

    private String serviceDate;
    private String customerName;
    private Item item;
    private String type;
    private String healthFund;
    private String paidAmt;
    private String claimedAmt;
}
