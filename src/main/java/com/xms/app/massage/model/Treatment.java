package com.xms.app.massage.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @ManyToMany
    @JoinTable(
            name="Treatment_Item",
            joinColumns = @JoinColumn(name = "treatment_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items;

    private BigDecimal expenseAmt;

    private BigDecimal claimedAmt;

    private String venue;

    private LocalDateTime createdDate;

    private boolean active = true;

    @Version
    private long version;

}
