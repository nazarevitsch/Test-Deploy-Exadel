package com.exadel.discount.platform.domain;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "user_details")
public class UserDetails {

    @Id
    @Column(name = "ud_id")
    @Type(type = "pg-uuid")
    private UUID id;

    @Column(name = "ud_name")
    private String name;

    @Type(type = "pg-uuid")
    @Column(name = "ud_location_id")
    private UUID locationId;

    @Column(name = "ud_user_id")
    @Type(type = "pg-uuid")
    private UUID userId;
}
