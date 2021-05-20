package com.nhatquang99.api.controller;

import com.nhatquang99.api.payload.response.GenericResponse;
import com.nhatquang99.api.service.IMailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
@Tag(name = "Mail Service")
public class MailController {
    @Autowired
    private IMailService mailService;

    @CrossOrigin
    @Operation(description = "Gởi email quên mật khẩu")
    @GetMapping("/password/{email}")
    public ResponseEntity<Object> forgotPassword(@PathVariable(name = "email") String email) {
        String token = UUID.randomUUID().toString();
        GenericResponse response = mailService.createPasswordResetTokenForUser(email, token);
        return ResponseEntity.ok(response);
    }

    @CrossOrigin
    @Operation(description = "Xác nhận email đổi mật khẩu", hidden = true)
    @GetMapping("/password/confirm/{email}/")
    public ResponseEntity<Object> validatePasswordResetToken(@PathVariable(name = "email") String email, @RequestParam String token, @RequestParam String password) throws MessagingException {
        GenericResponse response = mailService.validatePasswordResetToken(email, token, password);
        return ResponseEntity.ok(response);
    }

    @CrossOrigin
    @Operation(description = "Xác nhận email đăng ký", hidden = true)
    @GetMapping("/register/confirm/{email}")
    public ResponseEntity<Object> validateMailRegister(@PathVariable(name = "email") String email) {
        GenericResponse response = mailService.validateMailRegister(email);
        return ResponseEntity.ok(response);
    }

    @CrossOrigin
    @Operation(description = "Xác nhận email đăng ký", hidden = true)
    @GetMapping("/register/sent/{email}")
    public ResponseEntity<Object> sentMailRegister(@PathVariable(name = "email") String email) throws MessagingException {
        GenericResponse response = mailService.confirmCreateAccount(email);
        return ResponseEntity.ok(response);
    }
}
