package com.nhatquang99.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.UUID;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User extends Base {

    @Column(unique = true)
    private String username;

    @Column(unique = true,columnDefinition = "CHAR(100)")
    private String email;

    @Column(length = 60)
    private String password;

    @Column
    private String fullName;

    @Column
    private String address;

    @Column
    private String phoneNumber;

    @Column
    private boolean enable = true;

    @Column
    private boolean verifyMail = false;

    // mapping to Role
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "role_id",
            foreignKey = @ForeignKey(name = "fk_user_role")
    )
    private Role role;
}
