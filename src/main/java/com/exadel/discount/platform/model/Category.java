package com.exadel.discount.platform.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@SQLDelete(sql = "UPDATE category SET c_deleted=true WHERE c_id=?")
public class Category {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "c_id")
    private UUID id;
    @NotBlank
    @Size(min = 3, max = 50)
    @Column(name = "c_name")
    private String name;
    @OneToMany(mappedBy = "category")
    private List<SubCategory> subCategories = new ArrayList<>();
    @Column(name = "c_deleted")
    private boolean deleted;
}
