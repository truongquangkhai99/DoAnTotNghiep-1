package com.nhatquang99.api.controller;

import com.nhatquang99.api.payload.request.ProductRequest;
import com.nhatquang99.api.payload.response.GenericResponse;
import com.nhatquang99.api.payload.response.ProductResponse;
import com.nhatquang99.api.repository.ProductRepository;
import com.nhatquang99.api.service.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Tag(name = "Product Service")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IProductService productService;

    @CrossOrigin
    @GetMapping
    @Operation(description = "Tìm tất cả sản phẩm")
    public ResponseEntity<Object> findAll(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new GenericResponse(HttpStatus.OK, "Lấy list product thành công!", productService.findAll(pageable)));
    }

    @CrossOrigin
    @GetMapping("/{id}")
    @Operation(description = "Lấy sản phẩm theo Id")
    public ResponseEntity<Object> getProductById(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @CrossOrigin
    @GetMapping("/total")
    @Operation(description = "Tìm số lượng sản phẩm")
    public ResponseEntity<Object> getTotalProduct() {
        return ResponseEntity.ok(productService.getTotalProduct());
    }

    @CrossOrigin
    @GetMapping("/search")
    @Operation(description = "Tìm sản phẩm")
    public ResponseEntity<Object> findAll(@RequestParam(name = "categoryCode", required = false, defaultValue = "") String category,
                                          @RequestParam(name = "nameProduct", required = false, defaultValue = "") String name,
                                          @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                          @RequestParam(name = "size", required = false, defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new GenericResponse(HttpStatus.OK, "Tìm thành công!", productService.findByCategoryOrName(category, name, pageable)));
    }

    @CrossOrigin
    @PostMapping
    @Operation(description = "Thêm mới sản phẩm - cần có ROLE_ADMIN")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> createProduct(@RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(productService.create(productRequest));
    }

    @CrossOrigin
    @PutMapping("/{id}")
    @Operation(description = "Sửa sản phẩm - cần có ROLE_ADMIN")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> updateProduct(@PathVariable(name = "id") UUID id, @RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(productService.update(id, productRequest));
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    @Operation(description = "Xoá sản phẩm - cần có ROLE_ADMIN")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteProduct(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(productService.delete(id));
    }
}
