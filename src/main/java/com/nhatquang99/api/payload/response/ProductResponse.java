package com.nhatquang99.api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponse {
    private UUID id;
    private String nameProduct;
    private String description;
    private String typeCode;
    private int prices;
    private int quantity;
    private String categoryCode;
    private List<String> imgUrl;

    public ProductResponse() {

    }
}
