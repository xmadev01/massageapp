package com.xms.app.massage.controller;

import com.xms.app.massage.paging.Page;
import com.xms.app.massage.paging.PagingRequest;
import com.xms.app.massage.service.ProviderReportService;
import com.xms.app.massage.vo.ProviderReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes({"activeItem"})
public class ProviderReportController {

    @Autowired
    private ProviderReportService providerReportService;

    @GetMapping("/showProviderReport")
    public String show(Model model) {
        model.addAttribute("activeItem", "treatment");
        return "providerReport";
    }

    @PostMapping("/filterProviderReports")
    @ResponseBody
    public Page<ProviderReportVO> filterProviderReports(@RequestBody PagingRequest pagingRequest) {
        return providerReportService.getPage(pagingRequest);
    }
}
