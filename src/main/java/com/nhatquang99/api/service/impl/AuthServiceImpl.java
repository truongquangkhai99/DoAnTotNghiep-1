package com.nhatquang99.api.service.impl;

import com.nhatquang99.api.model.Role;
import com.nhatquang99.api.model.User;
import com.nhatquang99.api.model.enums.ERole;
import com.nhatquang99.api.payload.request.AuthRequest;
import com.nhatquang99.api.payload.response.GenericResponse;
import com.nhatquang99.api.payload.response.LoginResponse;
import com.nhatquang99.api.repository.RoleRepository;
import com.nhatquang99.api.repository.UserRepository;
import com.nhatquang99.api.security.JwtTokenProvider;
import com.nhatquang99.api.security.MyUserDetails;
import com.nhatquang99.api.service.IAuthService;
import com.nhatquang99.api.service.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements IAuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Object login(AuthRequest user) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            if (!userRepository.findByUsername(user.getUsername()).orElse(null).isVerifyMail())
                return new GenericResponse(HttpStatus.BAD_REQUEST,user.getUsername() + " chưa xác thực!", null);
            // Set thông tin authentication vào Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Trả về jwt cho người dùng.
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            String jwt = jwtTokenProvider.generateToken(userDetails);

            LoginResponse data = new LoginResponse(userDetails.getUsername(), jwt, userDetails.getAuthorities().stream().findFirst().get().toString());

            return new GenericResponse(HttpStatus.OK, "Đăng nhập thành công!", data);
        } catch (RuntimeException ex) {
            return new GenericResponse(HttpStatus.FORBIDDEN, "Đăng nhập thất bại. Tên đăng nhập hoặc mật khẩu không chính xác!", null);
        }
    }

    @Override
    public Object signup(AuthRequest userRegister) {
        if (userRepository.existsByUsername(userRegister.getUsername())) {
            return new GenericResponse(HttpStatus.BAD_REQUEST, "Đăng ký thất bại. " + userRegister.getUsername() + " đã tồn tại", null);
        }

        User user = new User();
        user.setPassword(passwordEncoder.encode(userRegister.getPassword()));
        user.setUsername(userRegister.getUsername());

        // Set role member cho user;
        Role role = roleRepository.findByName(ERole.ROLE_MEMBER).orElse(null);
        if (role == null) {
            return new GenericResponse(HttpStatus.BAD_REQUEST, ERole.ROLE_MEMBER + " không tồn tại.", null);
        }
        user.setRole(role);
        user.setVerifyMail(true);
        userRepository.save(user);

        return new GenericResponse(HttpStatus.OK, "Đăng ký thành công!", null);
    }
}
