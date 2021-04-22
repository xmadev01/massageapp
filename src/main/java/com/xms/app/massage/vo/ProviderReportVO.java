package com.xms.app.massage.vo;

import lombok.Data;

@Data
public class ProviderReportVO {

    private String serviceDate;
    private String practitioner;
    private String type;
    private String healthFund;
    private Integer numberOfMassageClient;
    private Integer numberOfAcupunctureClient;
    private String chargedMassageAmt;
    private String claimedMassageAmt;
    private String chargedAcupunctureAmt;
    private String claimedAcupunctureAmt;
}
