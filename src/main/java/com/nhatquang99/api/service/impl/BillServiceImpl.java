package com.nhatquang99.api.service.impl;

import com.nhatquang99.api.mapper.BillDetailMapper;
import com.nhatquang99.api.mapper.BillMapper;
import com.nhatquang99.api.model.Bill;
import com.nhatquang99.api.model.BillDetail;
import com.nhatquang99.api.model.Product;
import com.nhatquang99.api.model.User;
import com.nhatquang99.api.model.enums.EBillStatus;
import com.nhatquang99.api.payload.request.BillDetailRequest;
import com.nhatquang99.api.payload.request.BillRequest;
import com.nhatquang99.api.payload.response.BillResponse;
import com.nhatquang99.api.payload.response.GenericResponse;
import com.nhatquang99.api.payload.response.ListResponse;
import com.nhatquang99.api.repository.BillDetailRepository;
import com.nhatquang99.api.repository.BillRepository;
import com.nhatquang99.api.repository.ProductRepository;
import com.nhatquang99.api.repository.UserRepository;
import com.nhatquang99.api.service.IBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BillServiceImpl implements IBillService {
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillDetailRepository billDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BillMapper billMapper;

    private BillDetailMapper billDetailMapper = new BillDetailMapper();

    @Override
    public Object findAllBill(Pageable pageable) {
        Page<Bill> billPage = billRepository.findAll(pageable);
        List<Bill> bills = billPage.getContent();
        List<BillResponse> billResponses = new ArrayList<>();

        billResponses = this.toBillResponses(bills, billResponses);

        ListResponse listResponse = this.setListBillResponse(bills, billResponses);
        return new GenericResponse(HttpStatus.OK,"Lấy list bill thành công", listResponse);
    }

    @Override
    public Object findAllBillByUsername(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return new GenericResponse(HttpStatus.BAD_REQUEST, "Không tìm thấy username này!", null);
        }

        Page<Bill> billPage = billRepository.findAllByUser(user, pageable);
        List<Bill> bills = billPage.getContent();
        List<BillResponse> billResponses = new ArrayList<>();

        billResponses = this.toBillResponses(bills, billResponses);

        ListResponse listResponse = this.setListBillResponse(bills, billResponses);
        return new GenericResponse(HttpStatus.OK, "Lấy theo username thành công!", listResponse);
    }

    @Override
    public Object getBillById(UUID id) {
        Bill bill = billRepository.findById(id).orElse(null);
        if (bill == null) {
            return new GenericResponse(HttpStatus.BAD_REQUEST, "Lấy thất bại. " + id + " không tồn tại.", null);
        }

        BillResponse billResponse = new BillResponse();
        billResponse = billMapper.toBillResponse(bill, billResponse);

        return new GenericResponse(HttpStatus.OK, "Lấy thành công.", billResponse);
    }

    @Override
    @Transactional
    public GenericResponse createBill(String username, BillRequest billRequest) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return new GenericResponse(HttpStatus.BAD_REQUEST, "Tạo thất bại! User không tồn tại.", null);
        }

        Bill bill = new Bill();
        bill = billMapper.toBill(bill, billRequest, user);
        bill = billRepository.save(bill);

        long totalBill = 0;

        for (BillDetailRequest billDetailRequest : billRequest.getDetails()) {
            if (billDetailRequest.getQuantity() <= 0) {
                return new GenericResponse(HttpStatus.BAD_REQUEST, billDetailRequest.getProductID() + " số lượng phải lớn hơn 0.", null);
            }
            UUID productUUID = billDetailRequest.getProductID();
            Product product = productRepository.findById(productUUID).orElse(null);
            int quantityProduct = product.getQuantity();
            if (product == null) {
                return new GenericResponse(HttpStatus.BAD_REQUEST, billDetailRequest.getProductID() + " không tồn tại.", null);
            } else if (quantityProduct < billDetailRequest.getQuantity()) {
                return new GenericResponse(HttpStatus.BAD_REQUEST, product.getName() + " không đủ số lượng.", null);
            } else {
                BillDetail billDetail = new BillDetail();
                billDetail = billDetailMapper.toBillDetail(billDetail, billDetailRequest, bill, product);

                totalBill += billDetail.getTotalProduct();
                product.setQuantity(quantityProduct - billDetail.getQuantity());

                productRepository.save(product);
                billDetailRepository.save(billDetail);
            }
        }

        bill.setTotalBill(totalBill);
        billRepository.save(bill);
        return new GenericResponse(HttpStatus.OK, "Tạo đơn hàng thành công!", null);
    }

    @Override
    @Transactional
    public GenericResponse updateBill(String username, UUID id, BillRequest billRequest) {
        User user = userRepository.findByUsername(username).orElse(null);
        Bill bill = billRepository.findById(id).orElse(null);
        GenericResponse response = validateLogicUpdateBill(user, bill);
        if (response != null) {
            return response;
        }
        // Cập nhật đơn hàng
        bill = billMapper.toBill(bill, billRequest, user);

        List<BillDetail> billDetails = billDetailRepository.findAllByBill(bill);

        int sizeBillDetail = billDetails.size();
        int sizeBillDetailRequest = billRequest.getDetails().size();

        long totalBill = 0;
        for (int i = 0; i < sizeBillDetailRequest; i++) {
            BillDetailRequest billDetailRequest = billRequest.getDetails().get(i);
            UUID productUUID = billDetailRequest.getProductID();
            Product product = productRepository.findById(productUUID).orElse(null);
            if (product == null) {
                return new GenericResponse(HttpStatus.BAD_REQUEST, billDetailRequest.getProductID() + " không tồn tại.", null);
            }

            BillDetail billDetail;
            int quantityProduct;
            if (sizeBillDetail - (i + 1) >= 0) {
                billDetail = billDetails.get(i);
                quantityProduct = product.getQuantity() + billDetail.getQuantity();
            } else {
                billDetail = new BillDetail();
                quantityProduct = product.getQuantity();
            }

            if (quantityProduct < billDetailRequest.getQuantity()) {
                return new GenericResponse(HttpStatus.BAD_REQUEST, product.getName() + " không đủ số lượng.", null);
            }
            billDetail = billDetailMapper.toBillDetail(billDetail, billDetailRequest, bill, product);

            totalBill += billDetail.getTotalProduct();
            product.setQuantity(quantityProduct - billDetail.getQuantity());

            productRepository.save(product);
            billDetailRepository.save(billDetail);
        }

        bill.setTotalBill(totalBill);
        billRepository.save(bill);
        return new GenericResponse(HttpStatus.OK, "Cập nhật đơn hàng thành công!", null);
    }

    @Override
    public GenericResponse updateStatus(UUID id, EBillStatus billStatus) {
        Bill bill = billRepository.findById(id).orElse(null);
        if (bill == null) {
            new GenericResponse(HttpStatus.BAD_REQUEST, "Bill không tồn tại.", null);
        }
        bill.setStatus(billStatus);
        billRepository.save(bill);
        return new GenericResponse(HttpStatus.OK, "Cập nhật trạng thái đơn hàng thành công", null);
    }

    private GenericResponse validateLogicUpdateBill(User user, Bill bill) {
        if (user == null) {
            return new GenericResponse(HttpStatus.BAD_REQUEST, "Cập nhật thất bại! User không tồn tại.", null);
        }
        if (bill == null) {
            return new GenericResponse(HttpStatus.BAD_REQUEST, "Cập nhật thất bại! Bill không tồn tại.", null);
        }
        // Kiểm tra user có tồn tại bill đang sửa
        if (!bill.getUser().getId().equals(user.getId())) {
            return new GenericResponse(HttpStatus.BAD_REQUEST, "Cập nhật thất bại! User không có đơn hàng này.", null);
        }
        // Kiểm tra trạng thái đơn hàng
        if (!bill.getStatus().equals(EBillStatus.PENDING)) {
            return new GenericResponse(HttpStatus.BAD_REQUEST, "Đơn hàng này đã xác nhận bạn không được sửa.", null);
        }
        return null;
    }

    private List<BillResponse> toBillResponses(List<Bill> bills, List<BillResponse> billResponses) {
        bills.forEach(bill -> {
            BillResponse billResponse = new BillResponse();
            billResponse = billMapper.toBillResponse(bill, billResponse);
            billResponses.add(billResponse);
        });

        return billResponses;
    }

    private ListResponse setListBillResponse(List<Bill> bills, List<BillResponse> billResponses) {
        ListResponse listResponse = new ListResponse();
        listResponse.setNumberOfEntities(billRepository.count());
        listResponse.setSizeList(bills.size());
        listResponse.setList(billResponses);

        return listResponse;
    }

}
