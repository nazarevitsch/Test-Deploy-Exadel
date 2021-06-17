package com.exadel.discount.platform.model;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Tag {

    @Id
    @Type(type = "pg-uuid")
    @Column(name = "t_id")
    private UUID id;
    @NotBlank
    @Size(min = 3, max = 20)
    @Column(name = "t_name")
    private String name;
    @ManyToMany(mappedBy = "tags")
    @ToString.Exclude
    private List<Discount> discounts;
    @Column(name = "t_deleted")
    private boolean deleted;
}
