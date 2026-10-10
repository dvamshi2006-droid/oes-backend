package com.example.examination.service;

import com.example.examination.dto.UserDto;
import com.example.examination.entity.Role;
import com.example.examination.entity.User;
import com.example.examination.exception.BadRequestException;
import com.example.examination.exception.ResourceNotFoundException;
import com.example.examination.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDto> getAllStudents() {
        return userRepository.findByRole(Role.ROLE_STUDENT).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public UserDto getStudentById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        return mapToDto(user);
    }

    @Transactional
    public UserDto updateStudent(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new BadRequestException("Email is already taken");
            }
            user.setEmail(userDto.getEmail());
        }

        if (userDto.getStudentId() != null && !userDto.getStudentId().equals(user.getStudentId())) {
            if (userRepository.existsByStudentId(userDto.getStudentId())) {
                throw new BadRequestException("Student ID is already registered");
            }
            user.setStudentId(userDto.getStudentId());
        }

        if (userDto.getName() != null) user.setName(userDto.getName());
        if (userDto.getPhone() != null) user.setPhone(userDto.getPhone());

        User updatedUser = userRepository.save(user);
        return mapToDto(updatedUser);
    }

    @Transactional
    public void deleteStudent(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        if (user.getRole() == Role.ROLE_ADMIN) {
            throw new BadRequestException("Cannot delete administrator account");
        }
        userRepository.delete(user);
    }

    private UserDto mapToDto(User user) {
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
