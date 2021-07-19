package com.exadel.discount.platform.model;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@SQLDelete(sql = "UPDATE vendor_location SET vl_deleted=true WHERE vl_id=?")
public class VendorLocation {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "vl_id")
    private UUID id;
    @NotBlank
    @Size(min = 3, max = 60)
    @Column(name = "vl_country")
    private String country;
    @Size(min = 2, max = 50)
    @Column(name = "vl_city")
    private String city;
    @Size(min = 2, max = 50)
    @Column(name = "vl_address_line")
    private String addressLine;
    @NotNull
    @Column(name = "vendor_id")
    private UUID vendorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", insertable = false, updatable = false)
    private Vendor vendor;

    @Column(name = "vl_deleted")
    private boolean deleted;

    @Column(name = "vl_latitude")
    private double latitude;
    @Column(name = "vl_longitude")
    private double longitude;
}
