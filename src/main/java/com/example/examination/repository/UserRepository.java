package com.example.examination.repository;

import com.example.examination.entity.Role;
import com.example.examination.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByStudentId(String studentId);
    Boolean existsByEmail(String email);
    Boolean existsByStudentId(String studentId);
    List<User> findByRole(Role role);
    long countByRole(Role role);
}
