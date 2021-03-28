package com.xms.app.massage.model;

import com.xms.app.massage.enums.ServiceTypeEnum;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class MassageService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "customer", nullable = false)
    private Customer customer;

    private LocalDateTime serviceDateTime;

    private BigDecimal expenseAmt;

    private ServiceTypeEnum serviceType;

    private BigDecimal claimedAmt;

    private int duration;

    private String venue;

    private boolean active = true;

    @Version
    private long version;

}
