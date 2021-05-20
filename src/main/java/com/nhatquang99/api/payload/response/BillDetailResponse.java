package com.nhatquang99.api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class BillDetailResponse {
    private UUID id;
    private ProductResponse product;
    private long prices;
    private int quantity;
    private long totalProduct;

    public BillDetailResponse() {

    }
}
