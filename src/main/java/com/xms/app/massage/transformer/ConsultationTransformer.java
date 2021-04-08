package com.xms.app.massage.transformer;

import com.xms.app.massage.service.CustomerService;
import com.xms.app.massage.service.ItemService;
import com.xms.app.massage.service.PractitionerService;
import com.xms.app.massage.utils.CommonUtils;
import com.xms.app.massage.vo.ConsultationVO;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

@Component
public class ConsultationTransformer implements ResultTransformer {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private PractitionerService practitionerService;
    @Autowired
    private ItemService itemService;

    @Override
    public Object transformTuple(Object[] objects, String[] strings) {
        ConsultationVO consultationVo = new ConsultationVO();
        consultationVo.setServiceDate(formatDateStr(objects[0]));
        consultationVo.setCustomerName(customerService.loadCustomer(((Integer) objects[1]).longValue()).getFullName());
        consultationVo.setItem(itemService.findById(((BigInteger) objects[2]).longValue()).orElse(null));
        consultationVo.setPractitionerName(practitionerService.loadPractitioner(((Integer) objects[3]).longValue()).getFullName());
        consultationVo.setPaidAmt(CommonUtils.formatCurrencyData((Double) objects[4]));
        consultationVo.setClaimedAmt(CommonUtils.formatCurrencyData((Double) objects[5]));
        return consultationVo;
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
