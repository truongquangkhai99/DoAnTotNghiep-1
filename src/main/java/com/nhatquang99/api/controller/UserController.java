package com.nhatquang99.api.controller;

import com.nhatquang99.api.model.enums.ERole;
import com.nhatquang99.api.payload.request.UserRequest;
import com.nhatquang99.api.payload.response.GenericResponse;
import com.nhatquang99.api.payload.response.ListUserResponse;
import com.nhatquang99.api.payload.response.UserResponse;
import com.nhatquang99.api.service.IUserService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "User Service")
public class UserController {
    @Autowired
    private IUserService userService;

    @CrossOrigin
    @GetMapping
    @Operation(description = "Tìm tất cả User")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> findAll(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new GenericResponse(HttpStatus.OK, "Lấy list user thành công!", userService.findAll(pageable)));
    }

    @CrossOrigin
    @GetMapping("/{username}")
    @Operation(description = "Tìm User theo username")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> findByUsername(@PathVariable(name = "username") String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @CrossOrigin
    @GetMapping("/{username}/role")
    @Operation(description = "Cập nhật role của User theo username")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> updateRole(@PathVariable(name = "username") String username, @RequestParam ERole role) {
        GenericResponse response = userService.updateRole(username, role);
        return ResponseEntity.ok(response);
    }

    @CrossOrigin
    @PutMapping("/{username}/profile")
    @Operation(description = "Cập nhật profile của User theo username")
    public ResponseEntity<Object> updateProfileUser(@PathVariable(name = "username") String username, @RequestBody UserRequest user) {
        GenericResponse response = userService.updateProfileUser(username, user);
        return ResponseEntity.ok(response);
    }

    @CrossOrigin
    @DeleteMapping("/{username}")
    @Operation(description = "Disable User theo Username")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteUser(@PathVariable(name = "username") String username) {
        GenericResponse response = userService.delete(username);
        return ResponseEntity.ok(response);
    }
}
