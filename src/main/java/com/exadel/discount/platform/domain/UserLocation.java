package com.exadel.discount.platform.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table(name = "user_location")
public class UserLocation {

    @Id
    @Column(name = "ul_id")
    @org.hibernate.annotations.Type(type = "pg-uuid")
    private UUID id;

    @Column(name = "ul_country")
    private String country;

    @Column(name = "ul_city")
    private String city;
}
