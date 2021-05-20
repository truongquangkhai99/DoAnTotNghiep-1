package com.nhatquang99.api.service;

import com.nhatquang99.api.model.enums.EBillStatus;
import com.nhatquang99.api.payload.request.BillRequest;
import com.nhatquang99.api.payload.response.GenericResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IBillService {
    Object findAllBill(Pageable pageable);

    Object findAllBillByUsername(String email, Pageable pageable);

    Object getBillById(UUID id);

    GenericResponse createBill(String username, BillRequest billRequest);

    GenericResponse updateBill(String username, UUID id, BillRequest billRequest);

    GenericResponse updateStatus(UUID id, EBillStatus billStatus);


}
