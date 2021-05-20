package com.nhatquang99.api.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BillDetail extends Base{

    @Column
    @NotNull
    private long prices;

    @Column
    @NotNull
    private int quantity;

    @Column
    @NotNull
    private long totalProduct;

    @ManyToOne
    @EqualsAndHashCode.Exclude @ToString.Exclude
    @JoinColumn(
            name = "product_id",
            foreignKey = @ForeignKey(name = "fk_billDetail_product")
    )
    private Product product;

    @ManyToOne
    @EqualsAndHashCode.Exclude @ToString.Exclude
    @JoinColumn(
            name = "bill_id",
            foreignKey = @ForeignKey(name = "fk_billDetail_bill")
    )
    private Bill bill;
}
