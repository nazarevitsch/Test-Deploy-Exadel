package com.exadel.discount.platform.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "used_discount")
public class UsedDiscount {

    @Id
    @Type(type = "pg-uuid")
    @Column(name = "ud_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(name = "ud_user_id")
    private UUID userId;

    @Column(name = "ud_discount_id")
    private UUID discountId;

    @CreationTimestamp
    @Column(name = "ud_using_date")
    private ZonedDateTime usingDate;
}