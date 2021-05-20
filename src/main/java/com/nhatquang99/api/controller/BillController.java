package com.nhatquang99.api.controller;

import com.nhatquang99.api.model.enums.EBillStatus;
import com.nhatquang99.api.payload.request.BillRequest;
import com.nhatquang99.api.payload.response.GenericResponse;
import com.nhatquang99.api.service.IBillService;
import com.nhatquang99.api.validate.Validate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bill")
@RequiredArgsConstructor
@Tag(name = "Bill Service")
public class BillController {
    @Autowired
    private IBillService billService;

    private final Validate validate = new Validate();

    @CrossOrigin
    @GetMapping
    @Operation(description = "Tìm tất cả bill")
    public ResponseEntity<Object> findAllBill(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(billService.findAllBill(pageable));
    }

    @CrossOrigin
    @GetMapping("/search")
    @Operation(description = "Tìm tất cả bill của user")
    public ResponseEntity<Object> findAllBillByUser(@RequestParam(name = "username", required = false, defaultValue = "") String username,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(billService.findAllBillByUsername(username, pageable));
    }

    @CrossOrigin
    @GetMapping("/{id}")
    @Operation(description = "Lấy bill theo id")
    public ResponseEntity<Object> getBillById(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(billService.getBillById(id));
    }

    @CrossOrigin
    @PutMapping("/update/{id}")
    @Operation(description = "Cập nhật trạng thái bill")
    public ResponseEntity<Object> updateBillStatus(@PathVariable(name = "id") UUID id, @RequestParam(name = "status") EBillStatus billStatus) {
        return ResponseEntity.ok(billService.updateStatus(id, billStatus));
    }

    @CrossOrigin
    @PostMapping("/{username}")
    @Operation(description = "Thêm mới bill")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MEMBER')")
    public ResponseEntity<Object> createBill(@PathVariable(name = "username") String username, @RequestBody BillRequest billRequest) {
        GenericResponse response = validate.billValidate(billRequest);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(billService.createBill(username, billRequest));
    }

    @CrossOrigin
    @PutMapping("/{username}")
    @Operation(description = "Cập nhật bill")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MEMBER')")
    public ResponseEntity<Object> updateBill(@PathVariable(name = "username") String username, @RequestParam(name = "billid") UUID id, @RequestBody BillRequest billRequest) {
        GenericResponse response = validate.billValidate(billRequest);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(billService.updateBill(username, id, billRequest));
    }
}
