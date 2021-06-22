package com.exadel.discount.platform.model;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;

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
    @Type(type = "pg-uuid")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    @NotNull
    private Vendor vendor;

    @ManyToMany(mappedBy = "locations")
    @ToString.Exclude
    private List<Discount> discounts;
    @Column(name = "vl_deleted")
    private boolean deleted;
    /*private int latitude;
    private int longitude;*/

}
