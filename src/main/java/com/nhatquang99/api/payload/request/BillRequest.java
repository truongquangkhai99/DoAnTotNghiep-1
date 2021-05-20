package com.nhatquang99.api.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BillRequest {
    private String address;
    private String city;
    private String email;
    private String phoneNumber;
    private long totalBill;
    private List<BillDetailRequest> details;
}
