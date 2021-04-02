package com.xms.app.massage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String firstName;

    @Column
    private String middleName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate birthday;

    @Column
    private String email;

    @Column
    private String phone1;

    @Column
    private String phone2;

    @Column
    private String address;

    @Column
    private String postcode;

    @ManyToOne
    @JoinColumn(name="health_fund", nullable = false)
    private HealthFund healthFund;

    @Column
    private String membershipNum;

    @Column
    private Double rebateRate;

    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Treatment> treatments;

    @Column
    private String medication;

    private boolean active = true;

    @Version
    private long version;

    @Transient
    public String getFullName() {
        return firstName + " " + middleName + " " + lastName;
    }

}
