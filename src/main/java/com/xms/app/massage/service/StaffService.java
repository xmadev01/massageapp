package com.xms.app.massage.service;

import com.xms.app.massage.model.Staff;
import com.xms.app.massage.paging.Page;
import com.xms.app.massage.paging.PagingRequest;

import java.util.List;

public interface StaffService {

    void saveStaff(final Staff staff);

    Page<Staff> getPage(PagingRequest pagingRequest);

    Staff loadStaff(long staffId);

    void deactivateStaff(long staffId);

    void deleteStaff(long staffId);

    List<String> getAllStaff();
}
