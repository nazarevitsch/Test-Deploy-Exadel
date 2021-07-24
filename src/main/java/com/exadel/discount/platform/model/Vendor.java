package com.exadel.discount.platform.model;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.Email;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@SQLDelete(sql = "UPDATE vendor SET v_deleted=true WHERE v_id=?")
public class Vendor {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "v_id")
    private UUID id;
    @NotBlank
    @Size(min = 2, max = 50)
    @Column(name = "v_name")
    private String name;
    @Size(min = 50, max = 200)
    @Column(name = "v_description")
    private String description;
    @Email
    @Column(name = "v_email")
    private String email;
    @NotBlank
    @Column(name = "v_image")
    private String image;
    @OneToMany(mappedBy = "vendor", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<VendorLocation> vendorLocations;
    @Column(name = "v_deleted")
    private boolean deleted;
}
