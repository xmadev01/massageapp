package com.xms.app.massage.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotEmpty(message = "{staff.firstName.not.empty}")
    private String firstName;

    @Column(nullable = false)
    @NotEmpty(message = "{staff.lastName.not.empty}")
    private String lastName;

    private boolean active = true;

    @Version
    private long version;

    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }

}
