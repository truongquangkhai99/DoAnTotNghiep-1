package com.nhatquang99.api.service;

import com.nhatquang99.api.payload.request.AuthRequest;
import com.nhatquang99.api.payload.response.GenericResponse;

import javax.mail.MessagingException;

public interface IAuthService {
    Object login(AuthRequest user);
    Object signup(AuthRequest registerRequest) throws MessagingException;
}
