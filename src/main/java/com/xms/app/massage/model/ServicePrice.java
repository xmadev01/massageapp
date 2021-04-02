package com.xms.app.massage.model;

import com.xms.app.massage.enums.ServiceTypeEnum;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ServicePrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceTypeEnum serviceType;

    @Column(nullable = false)
    private Double price;

    private boolean active = true;

    @Version
    private long version;

}
