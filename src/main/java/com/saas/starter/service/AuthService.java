package com.saas.starter.service;

import com.saas.starter.dto.auth.AuthResponse;
import com.saas.starter.dto.auth.LoginRequest;
import com.saas.starter.dto.auth.RegisterRequest;
import com.saas.starter.entity.Role;
import com.saas.starter.entity.User;
import com.saas.starter.multitenancy.TenantContext;
import com.saas.starter.repository.RoleRepository;
import com.saas.starter.repository.UserRepository;
import com.saas.starter.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID is required");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setTenantId(tenantId);

        Role role = roleRepository.findByName(request.getRole())
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName(request.getRole());
                    newRole.setTenantId(tenantId);
                    return roleRepository.save(newRole);
                });

        user.getRoles().add(role);
        userRepository.save(user);

        return new AuthResponse(jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getUsername(), user.getPassword(), java.util.List.of()
                )));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsernameAndTenantId(request.getUsername(), TenantContext.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        return new AuthResponse(jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getUsername(), user.getPassword(), java.util.List.of()
                )));
    }
}
