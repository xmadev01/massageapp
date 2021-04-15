package com.xms.app.massage.transformer;

import com.xms.app.massage.dto.ConsultationDto;
import com.xms.app.massage.model.Item;
import com.xms.app.massage.service.CustomerService;
import com.xms.app.massage.service.HealthFundService;
import com.xms.app.massage.service.ItemService;
import com.xms.app.massage.utils.CommonUtils;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

@Component
public class ConsultationTransformer implements ResultTransformer {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private HealthFundService healthFundService;
    @Autowired
    private ItemService itemService;

    @Override
    public Object transformTuple(Object[] objects, String[] strings) {
        ConsultationDto consultationDto = new ConsultationDto();
        consultationDto.setServiceDate(formatDateStr(objects[0]));
        consultationDto.setCustomerName(customerService.loadCustomer(((Integer) objects[1]).longValue()).getFullName());
        final Item item = itemService.findById(((BigInteger) objects[2]).longValue()).get();
        consultationDto.setItem(item);
        consultationDto.setType(item.getType().getDisplayName());
        consultationDto.setHealthFund(healthFundService.loadHealthFund(((Integer) objects[3]).longValue()).getName());
        consultationDto.setPaidAmt((BigDecimal) objects[4]);
        consultationDto.setClaimedAmt((BigDecimal) objects[5]);
        return consultationDto;
    }

    @Override
    public List transformList(List list) {
        return list;
    }

    private String formatDateStr(Object obj) {
        Timestamp serviceDate = (Timestamp) obj;
        return CommonUtils.formatDateData(serviceDate);
    }
}
