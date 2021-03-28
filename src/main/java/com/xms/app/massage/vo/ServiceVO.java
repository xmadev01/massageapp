package com.xms.app.massage.vo;

import com.xms.app.massage.enums.ServiceTypeEnum;
import lombok.Data;

import java.util.List;

@Data
public class ServiceVO {

    private String customerName;
    private String serviceDate;
    private List<ServiceTypeEnum> serviceType;
}
