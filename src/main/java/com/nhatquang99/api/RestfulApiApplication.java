package com.nhatquang99.api;

import com.nhatquang99.api.model.Role;
import com.nhatquang99.api.model.User;
import com.nhatquang99.api.model.enums.ERole;
import com.nhatquang99.api.repository.RoleRepository;
import com.nhatquang99.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class RestfulApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestfulApiApplication.class, args);
    }

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (roleRepository.findByName(ERole.ROLE_MEMBER).orElse(null) == null) {
            Role roleMember = new Role(ERole.ROLE_MEMBER);
            roleRepository.save(roleMember);
        }
        if (roleRepository.findByName(ERole.ROLE_ADMIN).orElse(null) == null) {
            Role roleAdmin = new Role(ERole.ROLE_ADMIN);
            roleRepository.save(roleAdmin);
        }

        if (!userRepository.existsByUsername("admin")) {
            User user = new User();
            user.setUsername("admin");
            user.setEmail("admin@gmail.com");
            user.setPassword(passwordEncoder.encode("12345678"));
            user.setRole(roleRepository.findByName(ERole.ROLE_ADMIN).get());
            user.setEnable(true);
            user.setVerifyMail(true);

            userRepository.save(user);
        }
    }
}
