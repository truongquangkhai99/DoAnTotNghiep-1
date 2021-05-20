package com.nhatquang99.api.mapper;

import com.nhatquang99.api.model.Bill;
import com.nhatquang99.api.model.BillDetail;
import com.nhatquang99.api.model.User;
import com.nhatquang99.api.model.enums.EBillStatus;
import com.nhatquang99.api.payload.request.BillRequest;
import com.nhatquang99.api.payload.response.BillDetailResponse;
import com.nhatquang99.api.payload.response.BillResponse;
import com.nhatquang99.api.repository.BillDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BillMapper {

    @Autowired
    private BillDetailRepository billDetailRepository;

    private final BillDetailMapper billDetailMapper = new BillDetailMapper();

    public Bill toBill(Bill bill, BillRequest request, User user) {
        bill.setAddress(request.getAddress());
        bill.setCity(request.getCity());
        bill.setPhoneNumber(request.getPhoneNumber());
        bill.setStatus(EBillStatus.PENDING);
        bill.setTotalBill(request.getTotalBill());
        bill.setUser(user);

        return bill;
    }

    public BillResponse toBillResponse(Bill bill, BillResponse billResponse) {
        billResponse.setId(bill.getId());
        billResponse.setCity(bill.getCity());
        billResponse.setPhoneNumber(bill.getPhoneNumber());
        billResponse.setEmail(bill.getUser().getEmail());
        billResponse.setTotalBill(bill.getTotalBill());
        billResponse.setAddress(bill.getAddress());

        // Mapper details
        List<BillDetail> billDetails = billDetailRepository.findAllByBill(bill);
        List<BillDetailResponse> billDetailResponses = new ArrayList<>();
        billDetailResponses = billDetailMapper.toBillResponses(billDetails, billDetailResponses);
        billResponse.setDetails(billDetailResponses);

        billResponse.setBillStatus(bill.getStatus());
        return billResponse;
    }


}
