package com.achraf.videogames.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;
import java.util.List;



@Service
@RequiredArgsConstructor
public class UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    @Transactional
    public void save(User user) {


        var userRole = roleRepository.findByRole(RoleType.ROLE_ADMIN.name())
                .orElse(
                        Role.builder()
                                .role(RoleType.ROLE_ADMIN.name())
                                .build()
                );
        if (userRole.getUsers() == null) {
            userRole = roleRepository.save(userRole);
        }
        var defaultUserRole = List.of(userRole);
        user.setRoles(defaultUserRole);
        var savedUser = userRepository.save(user);
        savedUser.setPassword(encoder.encode(savedUser.getPassword()));
        userRole.setUsers(new ArrayList<>(List.of(savedUser)));
        roleRepository.save(userRole);

    }

}