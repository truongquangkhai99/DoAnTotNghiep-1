package com.nhatquang99.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nhatquang99.api.model.enums.EBillStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Bill extends Base{

    @Column
    @NotBlank
    private String address;

    @Column
    @NotBlank
    private String city;

    @Column
    @NotBlank
    private String phoneNumber;

    @Column
    @NotNull
    private long totalBill;

    @Column
    @Enumerated(EnumType.STRING)
    private EBillStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(name = "fk_bill_user")
    )
    private User user;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    private List<BillDetail> billDetails;
}
