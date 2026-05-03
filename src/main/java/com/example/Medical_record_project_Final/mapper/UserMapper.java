package com.example.Medical_record_project_Final.mapper;

import com.example.Medical_record_project_Final.data.entity.Role;
import com.example.Medical_record_project_Final.data.entity.User;
import com.example.Medical_record_project_Final.dto.UserEditDto;
import com.example.Medical_record_project_Final.dto.UserRegisterDto;

public final class UserMapper {

    private UserMapper() {
    }

    /**
     * Plain-text password; {@link com.example.Medical_record_project_Final.data.service.UserService#create(User)} encodes it.
     */
    public static User toRegisterEntity(UserRegisterDto dto, Role role) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setEnabled(true);
        user.setRole(role);
        return user;
    }

    public static User toEntity(UserRegisterDto dto, Role role, String encodedPassword) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(encodedPassword);
        user.setEmail(dto.getEmail());
        user.setEnabled(true);
        user.setRole(role);
        return user;
    }

    public static void updateEntity(User user, UserEditDto dto, Role role, String encodedPassword) {
        user.setUsername(dto.getUsername());
        user.setPassword(encodedPassword);
        user.setEmail(dto.getEmail());
        user.setEnabled(dto.isEnabled());
        user.setRole(role);
    }
}