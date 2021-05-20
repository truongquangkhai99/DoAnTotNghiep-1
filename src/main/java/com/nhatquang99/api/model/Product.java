package com.nhatquang99.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Product extends Base{

    @Column
    @NotBlank
    private String name;

    @Column
    @NotBlank
    private String content;

    @Column
    @NotBlank
    private String type;

    @Column
    @NotNull
    private int price;

    @Column
    @NotNull
    private int quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "category_id",
            foreignKey = @ForeignKey(name = "fk_product_category")
    )
    private Category category;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    private List<ProductImage> listImage;
}
