package com.exadel.discount.platform.domain;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class User {

    @Id
    @Column(name = "u_id")
    @Type(type = "pg-uuid")
    private UUID id;

    @Column(name = "u_email")
    private String email;

    @ToString.Exclude
    @Column(name = "u_password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "u_user_role")
    @Type(type = "pgsql_enum")
    private UserRole userRole;
}
