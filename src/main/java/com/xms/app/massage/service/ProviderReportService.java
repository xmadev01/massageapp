package com.xms.app.massage.service;

import com.xms.app.massage.paging.Page;
import com.xms.app.massage.paging.PagingRequest;
import com.xms.app.massage.vo.ProviderReportVO;

public interface ProviderReportService {

    Page<ProviderReportVO> getPage(PagingRequest pagingRequest);

}
