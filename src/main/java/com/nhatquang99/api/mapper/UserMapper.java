package com.nhatquang99.api.mapper;



import com.nhatquang99.api.model.User;
import com.nhatquang99.api.payload.request.UserRequest;
import com.nhatquang99.api.payload.response.UserResponse;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public UserResponse toUserRes(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setAddress(user.getAddress());
        userResponse.setFullName(user.getFullName());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setStatus(user.isEnable());
        userResponse.setRole(user.getRole().getName());

        return userResponse;
    }

    public List<UserResponse> toListUserRes(List<User> users) {
        List<UserResponse> responses = new ArrayList<>();
        users.forEach(user -> responses.add(this.toUserRes(user)));

        return responses;
    }

}
