package com.exadel.discount.platform.domain;

import com.exadel.discount.platform.model.Category;
import com.exadel.discount.platform.model.SubCategory;
import com.exadel.discount.platform.model.Vendor;
import com.exadel.discount.platform.model.VendorLocation;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "discount")
@SQLDelete(sql = "UPDATE discount SET d_is_deleted=true WHERE d_id=?")
public class Discount {

    @Id
    @Type(type = "pg-uuid")
    @Column(name = "d_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(name = "d_name")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "discount_vendor_location",
            joinColumns = @JoinColumn(name = "dvl_discount_id"),
            inverseJoinColumns = @JoinColumn(name = "dvl_vendor_location_id")
    )
    private List<VendorLocation> vendorLocations;

    @ManyToMany
    @JoinTable(
            name = "discount_sub_category",
            joinColumns = @JoinColumn(name = "dsc_discount_id"),
            inverseJoinColumns = @JoinColumn(name = "dsc_sub_category_id")
    )
    private List<SubCategory> subCategories;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "d_category_id", referencedColumnName = "c_id", insertable = false, updatable = false)
    private Category category;

    @NotNull
    @Column(name = "d_category_id")
    private UUID categoryId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "d_vendor_id", referencedColumnName = "v_id", insertable = false, updatable = false)
    private Vendor vendor;

    @NotNull
    @Column(name = "d_vendor_id")
    private UUID vendorId;

    @Size(min = 50, max = 2000)
    @Column(name = "d_full_description")
    private String fullDescription;

    @Column(name = "d_is_online")
    private boolean isOnline;

    @Size(min = 10, max = 150)
    @Column(name = "d_image_Link")
    private String imageLink;

    @Column(name = "d_start_date")
    private ZonedDateTime startDate;

    @Column(name = "d_end_date")
    private ZonedDateTime endDate;

    @Column(name = "d_is_deleted")
    private boolean isDeleted;

    @Min(1)
    @Max(99)
    @Column(name = "d_percentage")
    private int percentage;

    @Column(name = "d_usage_count")
    private int usageCount;
}
