package com.nhatquang99.api.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductImage extends Base{

    @Column
    private String url;

    @ManyToOne
    @EqualsAndHashCode.Exclude @ToString.Exclude
    @JoinColumn(
            name = "product_id",
            foreignKey = @ForeignKey(name = "fk_productImage_product")
    )
    private Product product;
}
