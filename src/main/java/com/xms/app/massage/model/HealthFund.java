package com.xms.app.massage.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class HealthFund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    private boolean active = true;

    @Version
    private long version;

}
