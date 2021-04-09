package com.xms.app.massage.paging;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class PagingRequest {

    private int start;
    private int length;
    private int draw;
    private List<Order> order;
    private List<Column> columns;
    private Search search;
    private String viewMode;
    private LocalDate currentDay;
    private String currentMonth;
    private String currentYear;
}
