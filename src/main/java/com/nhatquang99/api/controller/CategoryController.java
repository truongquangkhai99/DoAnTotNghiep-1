package com.nhatquang99.api.controller;

import com.nhatquang99.api.model.Category;
import com.nhatquang99.api.payload.request.CategoryRequest;
import com.nhatquang99.api.payload.response.GenericResponse;
import com.nhatquang99.api.service.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
@Tag(name = "Category Service")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @CrossOrigin
    @GetMapping
    @Operation(description = "Tìm tất cả danh mục")
    public ResponseEntity<Object> findAll() {
        return ResponseEntity.ok(new GenericResponse(HttpStatus.OK, "Lấy list category thành công!", categoryService.findAll()));
    }

    @CrossOrigin
    @PostMapping
    @Operation(description = "Tạo mới danh mục - cần có ROLE_ADMIN")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> createCategory(@RequestBody CategoryRequest category) {
        return ResponseEntity.ok(categoryService.create(category));
    }

    @CrossOrigin
    @PutMapping("/{id}")
    @Operation(description = "Sửa tên hoặc cha của danh mục - cần có ROLE_ADMIN")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> updateCategory(@PathVariable(name = "id") UUID uuid, @RequestBody CategoryRequest category) {
        return ResponseEntity.ok(categoryService.update(uuid, category));
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    @Operation(description = "Xoá danh mục - cần có ROLE_ADMIN")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteCategory(@PathVariable(name = "id") UUID uuid) {
        return ResponseEntity.ok(categoryService.delete(uuid));
    }
}
