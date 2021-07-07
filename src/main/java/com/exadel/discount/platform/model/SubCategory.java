package com.exadel.discount.platform.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Data
@Table(name = "sub_category")
@SQLDelete(sql = "UPDATE sub_category SET sc_deleted=true WHERE sc_id=?")
public class SubCategory {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "sc_id")
    private UUID id;
    @NotBlank
    @Size(min = 3, max = 20)
    @Column(name = "sc_name")
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "sc_deleted")
    private boolean deleted;
}
