package com.nhatquang99.api.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRequest {
    private String email;
    private String fullName;
    private String address;
    private String phoneNumber;

    public UserRequest() {

    }
}
