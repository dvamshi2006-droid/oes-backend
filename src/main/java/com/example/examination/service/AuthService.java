package com.example.examination.service;

import com.example.examination.dto.AuthRequest;
import com.example.examination.dto.AuthResponse;
import com.example.examination.dto.RegisterRequest;
import com.example.examination.dto.UserDto;
import com.example.examination.entity.Role;
import com.example.examination.entity.User;
import com.example.examination.exception.BadRequestException;
import com.example.examination.repository.UserRepository;
import com.example.examination.security.JwtTokenProvider;
import com.example.examination.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email address is already in use");
        }

        if (request.getStudentId() != null && userRepository.existsByStudentId(request.getStudentId())) {
            throw new BadRequestException("Student ID is already registered");
        }

        User user = new User(
                request.getName(),
                request.getStudentId(),
                request.getEmail(),
                request.getPhone(),
                passwordEncoder.encode(request.getPassword()),
                Role.ROLE_STUDENT
        );

        User savedUser = userRepository.save(user);

        // Auto authenticate and issue token
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        return new AuthResponse(
                jwt,
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getStudentId(),
                savedUser.getRole().name()
        );
    }

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return new AuthResponse(
                jwt,
                userPrincipal.getId(),
                userPrincipal.getName(),
                userPrincipal.getEmail(),
                userPrincipal.getStudentId(),
                userPrincipal.getRole().name()
        );
    }

    public UserDto getCurrentUser(UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new BadRequestException("User profile not found"));

        return new UserDto(
                user.getId(),
                user.getName(),
                user.getStudentId(),
                user.getEmail(),
                user.getPhone(),
                user.getRole().name(),
                user.getCreatedAt()
        );
    }
}
