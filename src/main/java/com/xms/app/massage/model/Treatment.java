package com.xms.app.massage.model;

import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Treatment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "customer", nullable = false)
    private Customer customer;

    private LocalDate serviceDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item")
    private Item item;

    @Column
    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "practitioner", nullable = false)
    private Practitioner practitioner;

    private BigDecimal expenseAmt;

    private BigDecimal claimedAmt;

    private String venue;

    private String medicalCaseRecord;

    private LocalDateTime createdDate;

    private boolean active = true;

    @Version
    private long version;

    @Transient
    public String getItemDisplayName() {
        if (duration != null) {
            return item.getDisplayName() + " - " + duration + " min";
        } else {
            return item.getDisplayName();
        }
    }
}
