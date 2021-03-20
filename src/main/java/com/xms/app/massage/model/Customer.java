package com.xms.app.massage.model;

import com.xms.app.massage.enums.HealthFundEnum;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column
    private String phone;

    @Column
    private String mobile;

    @Column
    private String address;

    @Enumerated(EnumType.ORDINAL)
    private HealthFundEnum healthFund;

    private boolean active = true;

    @Version
    private long version;

    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }

}
