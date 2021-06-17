package com.exadel.discount.platform.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Discount {

    @Id
    @Type(type = "pg-uuid")
    @Column(name = "d_id")
    private UUID id;
    @NotBlank
    @Size(min = 2, max = 50)
    @Column(name = "d_name")
    private String name;
    @ManyToMany
    @JoinTable(
            name = "discount_tag",
            joinColumns = @JoinColumn(name = "discount_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @ManyToMany
    @JoinTable(
            name = "discount_location",
            joinColumns = @JoinColumn(name = "discount_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    @NotEmpty
    private List<VendorLocation> vendorLocations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;
    @Size(min = 50, max = 200)
    @Column(name = "d_full_description")
    private String fullDescription;
    @Column(name = "d_is_active")
    private boolean isActive;
    @Column(name = "d_is_online")
    private boolean isOnline;
    @Size(min = 2, max = 80)
    @Column(name = "d_image_Link")
    private String imageLink;
    @Column(name = "d_start_date")
    private ZonedDateTime startDate;
    @Column(name = "d_end_date")
    private ZonedDateTime endDate;
    @Column(name = "d_deleted")
    private boolean deleted;
}
