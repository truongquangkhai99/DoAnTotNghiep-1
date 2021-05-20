package com.nhatquang99.api.service;

import com.nhatquang99.api.model.enums.ERole;
import com.nhatquang99.api.payload.request.UserRequest;
import com.nhatquang99.api.payload.response.GenericResponse;
import com.nhatquang99.api.payload.response.ListResponse;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    ListResponse findAll(Pageable pageable);

    Object findByUsername(String username);

    GenericResponse updateRole(String username, ERole role);

    GenericResponse updateProfileUser(String username, UserRequest user);

    GenericResponse delete(String username);
}
