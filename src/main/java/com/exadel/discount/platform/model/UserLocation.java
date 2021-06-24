package com.exadel.discount.platform.model;

import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Data
@Table(name = "user_location")
@SQLDelete(sql = "UPDATE user_location SET u_location_deleted=true WHERE u_location_id=?")
public class UserLocation {

    @Id
    @Type(type = "pg-uuid")
    @Column(name = "u_location_id")
    private UUID id;
    @NotBlank
    @Size(min = 3, max = 60)
    @Column(name = "u_location_country")
    private String country;
    @Size(min = 2, max = 50)
    @Column(name = "u_location_city")
    private String city;
    @Size(min = 2, max = 50)
    @Column(name = "u_location_address_line")
    private String addressLine;
    /*@OneToMany(mappedBy = "userLocation")
    private MyUserDetails userDetails;*/
    @Column(name = "u_location_deleted")
    private boolean deleted;
    /*private int latitude;
    private int longitude;*/
}
