package com.xms.app.massage.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Treatment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "customer", nullable = false)
    private Customer customer;

    private LocalDate serviceDate;

    @OneToOne
    @JoinColumn(name = "item")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "practitioner", nullable = false)
    private Practitioner practitioner;

    private BigDecimal expenseAmt;

    private BigDecimal claimedAmt;

    private String venue;

    private LocalDateTime createdDate;

    private boolean active = true;

    @Version
    private long version;

}
