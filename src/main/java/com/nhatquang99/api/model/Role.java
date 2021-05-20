package com.nhatquang99.api.model;

import com.nhatquang99.api.model.enums.ERole;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
public class Role extends Base {

    @Column
    @Enumerated(EnumType.STRING)
    private ERole name;

    // Mapping to user
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    private Set<User> users;

    public Role(ERole name) {
        this.name = name;
    }

    public Role() {

    }
}
