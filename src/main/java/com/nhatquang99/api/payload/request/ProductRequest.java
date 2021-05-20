package com.nhatquang99.api.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProductRequest {
    private String nameProduct;
    private String description;
    private String typeCode;
    private int prices;
    private int quantity;
    private String categoryCode;
    private List<String> imgUrl;
}
