package com.xms.app.massage.model;

import com.xms.app.massage.enums.MassageTypeEnum;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Customer customer;

    private LocalDateTime serviceDateTime;

    private BigDecimal expenseAmt;

    private MassageTypeEnum massageType;

    private BigDecimal claimedAmt;

    private int duration;

    private String venue;

    private boolean active = true;

    @Version
    private long version;

}
