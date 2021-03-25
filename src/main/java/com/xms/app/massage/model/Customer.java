package com.xms.app.massage.model;

import com.xms.app.massage.enums.HealthFundEnum;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotEmpty(message = "{customer.firstName.not.empty}")
    private String firstName;

    @Column(nullable = false)
    @NotEmpty(message = "{customer.lastName.not.empty}")
    private String lastName;

    @Column
    @Email(message = "{customer.email.invalid}")
    private String email;

    @Column
    private String phone;

    @Column
    private String mobile;

    @Column
    private String address;

    @Enumerated(EnumType.ORDINAL)
    private HealthFundEnum healthFund;

    @Column
    private String membershipNum;

    @Column
    private Double rebateRate;

    @OneToMany(mappedBy = "customer")
    private List<Service> services;

    private boolean active = true;

    @Version
    private long version;

    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }

}
