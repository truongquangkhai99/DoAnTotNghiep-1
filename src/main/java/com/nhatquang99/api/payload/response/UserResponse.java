package com.nhatquang99.api.payload.response;

import com.nhatquang99.api.model.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private String username;
    private String email;
    private String fullName;
    private String address;
    private String phoneNumber;
    private boolean status;
    private ERole role;

    public UserResponse() {

    }
}
