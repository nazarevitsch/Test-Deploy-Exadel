package com.exadel.discount.platform.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
public class FavoriteDiscount {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "fd_id")
    private UUID id;
    @NotNull
    @Column(name = "fd_discount_id")
    private UUID discountId;
    @NotNull
    @Column(name = "fd_user_id")
    private UUID userId;
    @NotNull
    @Column(name = "fd_like_date")
    private ZonedDateTime likeDate;
}
