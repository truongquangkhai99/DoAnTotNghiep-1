package com.nhatquang99.api.payload.response;

import com.nhatquang99.api.model.enums.EBillStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class BillResponse {
    private UUID id;
    private String email;
    private String address;
    private String city;
    private String phoneNumber;
    private long totalBill;
    private List<BillDetailResponse> details;
    private EBillStatus billStatus;
    public BillResponse() {

    }
}
