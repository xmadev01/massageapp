package com.xms.app.massage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @Column
    private String lastName;

    @Column
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
    @JoinColumn(name="health_fund")
    private HealthFund healthFund;

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
        if (StringUtils.isNotBlank(middleName)) {
            return firstName + " " + middleName + " " + lastName;
        } else if (StringUtils.isNotBlank(lastName)) {
            return firstName + " " + lastName;
        } else {
            return firstName;
        }
    }

    @Transient
    public String getFullNameBirthDayId() {
        if (StringUtils.isNotBlank(middleName)) {
            return firstName + " " + middleName + " " + lastName + " - " + birthday.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " - " + id;
        } else if (StringUtils.isNotBlank(lastName)) {
            return firstName + " " + lastName  + " - " + birthday.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " - " + id;
        } else {
            return firstName + " - " + birthday.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " - " + id;
        }
    }

}
