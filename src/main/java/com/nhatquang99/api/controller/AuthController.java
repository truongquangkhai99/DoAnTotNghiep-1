package com.nhatquang99.api.controller;

import com.nhatquang99.api.payload.request.AuthRequest;
import com.nhatquang99.api.payload.response.GenericResponse;
import com.nhatquang99.api.payload.response.LoginResponse;
import com.nhatquang99.api.service.IAuthService;
import com.nhatquang99.api.validate.Validate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Service")
public class AuthController {
    @Autowired
    private IAuthService authService;
    private Validate validate = new Validate();

    @CrossOrigin
    @Operation(description = "Đăng nhập")
    @PostMapping("/login")
    public ResponseEntity<Object> login(
            @Parameter(
                    description = "Use 'Auth' model to login",
                    required = true,
                    schema = @Schema(implementation = AuthRequest.class))
            @RequestBody() AuthRequest loginRequest) {

        GenericResponse genericResponse = validate.authValidate(loginRequest);
        if (genericResponse != null) {
            return ResponseEntity.ok(genericResponse);
        }
        return new ResponseEntity<Object>(authService.login(loginRequest), HttpStatus.OK);
    }

    @CrossOrigin
    @Operation(description = "Đăng ký")
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(
            @Valid
            @Parameter(
                    description = "Use 'Auth' model to register",
                    required = true,
                    schema = @Schema(implementation = AuthRequest.class))
            @RequestBody AuthRequest registerRequest) throws MessagingException {
        GenericResponse genericResponse = validate.authValidate(registerRequest);
        if (genericResponse != null) {
            return ResponseEntity.ok(genericResponse);
        }
        return ResponseEntity.ok(authService.signup(registerRequest));
    }
}
