package com.nhatquang99.api.mapper;

import com.nhatquang99.api.model.Bill;
import com.nhatquang99.api.model.BillDetail;
import com.nhatquang99.api.model.Product;
import com.nhatquang99.api.payload.request.BillDetailRequest;
import com.nhatquang99.api.payload.response.BillDetailResponse;

import java.util.List;

public class BillDetailMapper {

    private final ProductMapper productMapper = new ProductMapper();

    public BillDetail toBillDetail(BillDetail billDetail, BillDetailRequest request, Bill bill, Product product) {
        long prices = request.getPrices();
        int quantity = request.getQuantity();

        billDetail.setPrices(prices);
        billDetail.setQuantity(quantity);
        billDetail.setTotalProduct(prices * quantity);
        billDetail.setProduct(product);
        billDetail.setBill(bill);

        return billDetail;
    }

    public BillDetailResponse toBillResponse(BillDetail billDetail, BillDetailResponse billDetailResponse) {
        billDetailResponse.setId(billDetail.getId());
        billDetailResponse.setProduct(productMapper.toProductRes(billDetail.getProduct()));
        billDetailResponse.setQuantity(billDetail.getQuantity());
        billDetailResponse.setPrices(billDetail.getPrices());
        billDetailResponse.setTotalProduct(billDetail.getTotalProduct());

        return billDetailResponse;
    }

    public List<BillDetailResponse> toBillResponses(List<BillDetail> billDetails, List<BillDetailResponse> billDetailResponses) {
        billDetails.forEach(billDetail -> {
            BillDetailResponse billDetailResponse = new BillDetailResponse();
            billDetailResponses.add(this.toBillResponse(billDetail, billDetailResponse));
        });

        return billDetailResponses;
    }
}
