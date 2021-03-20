package com.xms.app.massage.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="userCode", nullable = false, unique = true)
    private String userCode;

    @Column(nullable = false, unique = true)
    private String password;

    @Column(name="firstName", nullable = false)
    private String firstName;

    @Column(name="lastName", nullable = false)
    private String lastName;

    private boolean active = true;

    @Version
    private long version;

    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }

}
