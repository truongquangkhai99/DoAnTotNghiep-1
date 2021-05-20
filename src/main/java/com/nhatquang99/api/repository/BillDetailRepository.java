package com.nhatquang99.api.repository;

import com.nhatquang99.api.model.Bill;
import com.nhatquang99.api.model.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail, UUID> {
    List<BillDetail> findAllByBill(Bill bill);
}
