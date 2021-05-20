package com.nhatquang99.api.service.impl;

import com.nhatquang99.api.mapper.UserMapper;
import com.nhatquang99.api.model.Role;
import com.nhatquang99.api.model.User;
import com.nhatquang99.api.model.enums.ERole;
import com.nhatquang99.api.payload.request.UserRequest;
import com.nhatquang99.api.payload.response.GenericResponse;
import com.nhatquang99.api.payload.response.ListResponse;
import com.nhatquang99.api.payload.response.UserResponse;
import com.nhatquang99.api.repository.RoleRepository;
import com.nhatquang99.api.repository.UserRepository;
import com.nhatquang99.api.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final UserMapper userMapper = new UserMapper();

    @Override
    public ListResponse findAll(Pageable pageable) {
        // Truy cập vào database lấy ra list user đã phân trang
        Page<User> users = userRepository.findAll(pageable);

        List<UserResponse> userResponses = userMapper.toListUserRes(users.getContent());

        ListResponse response = new ListResponse();
        response.setNumberOfEntities(userRepository.count());
        response.setSizeList(userResponses.size());
        response.setList(userResponses);

        return response;
    }

    @Override
    public Object findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return new GenericResponse(HttpStatus.BAD_REQUEST, "Không tìm thấy user: " + username, null);
        }
        return new GenericResponse(HttpStatus.OK, "Thành công.", userMapper.toUserRes(user));
    }

    @Override
    public GenericResponse updateRole(String username, ERole nameRole) {
        Role role = roleRepository.findByName(nameRole).orElse(null);
        if (role == null) {
            return new GenericResponse(HttpStatus.BAD_REQUEST, nameRole + " không tìm thấy.", null);
        }
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return new GenericResponse(HttpStatus.BAD_REQUEST, username + " không tìm thấy.", null);
        }
        user.setRole(role);
        userRepository.save(user);
        return new GenericResponse(HttpStatus.OK, "Cập nhật role thành công.", null);
    }

    @Override
    public GenericResponse updateProfileUser(String username, UserRequest userReq) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return new GenericResponse(HttpStatus.BAD_REQUEST, username + " không tìm thấy.", null);
        }
        user.setAddress(userReq.getAddress());
        user.setFullName(userReq.getFullName());
        user.setPhoneNumber(userReq.getPhoneNumber());
        user.setEmail(userReq.getEmail());

        userRepository.save(user);
        return new GenericResponse(HttpStatus.OK, "Cập nhật thông tin thành công.", null);
    }

    @Override
    public GenericResponse delete(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return new GenericResponse(HttpStatus.BAD_REQUEST, username + " không tìm thấy.", null);
        }
        user.setEnable(false);
        userRepository.save(user);
        return new GenericResponse(HttpStatus.OK, "Disable user thành công.", null);
    }
}
