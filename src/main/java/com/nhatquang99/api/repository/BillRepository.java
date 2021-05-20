package com.nhatquang99.api.repository;

import com.nhatquang99.api.model.Bill;
import com.nhatquang99.api.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BillRepository extends JpaRepository<Bill, UUID> {
    Page<Bill> findAll(Pageable pageable);
    Page<Bill> findAllByUser(User user, Pageable pageable);

    @Query(value = "SELECT month(bill.updated_at) as 'month', sum(bill.total_bill) as 'total' " +
            "FROM bill where Year(bill.updated_at) = ?1 AND bill.status = 'PAIR' " +
            "group by month(bill.updated_at) order by month(bill.updated_at)", nativeQuery = true)
    List<Object> selectTotalsOfMonth(int year);

    @Query(value = "SELECT day(bill.updated_at) as 'month', sum(bill.total_bill) as 'total' " +
            "FROM bill where month(bill.updated_at) = ?1 AND year(bill.updated_at) = ?2 AND bill.status = 'PAIR' " +
            "group by day(bill.updated_at) order by day(bill.updated_at)", nativeQuery = true)
    List<Object> selectTotalsOfDay(int month, int year);
}
