package com.nhatquang99.api.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class BillDetailRequest {
    private UUID productID;
    private long prices;
    private int quantity;
    private long totalProduct;
}
