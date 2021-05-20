package com.nhatquang99.api.service;


import com.nhatquang99.api.payload.response.GenericResponse;

import javax.mail.MessagingException;

public interface IMailService {
    GenericResponse validatePasswordResetToken(String email, String token, String password) throws MessagingException;
    GenericResponse createPasswordResetTokenForUser(String email, String token);
    GenericResponse confirmCreateAccount(String email) throws MessagingException;
    GenericResponse validateMailRegister(String email);

}
