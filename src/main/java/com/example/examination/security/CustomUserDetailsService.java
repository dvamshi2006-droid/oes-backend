package com.example.examination.security;

import com.example.examination.entity.User;
import com.example.examination.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String emailOrStudentId) throws UsernameNotFoundException {
        // Try finding by email or student ID
        User user = userRepository.findByEmail(emailOrStudentId)
                .orElseGet(() -> userRepository.findByStudentId(emailOrStudentId)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email or studentId: " + emailOrStudentId)));

        return UserPrincipal.create(user);
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        return UserPrincipal.create(user);
    }
}
