package com.example.Medical_record_project_Final.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserEditDto {

    @NotNull(message = "Id is required.")
    private Integer id;

    @NotBlank(message = "Username is required.")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters.")
    private String username;

    @NotBlank(message = "Password is required.")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters.")
    private String password;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    @Size(max = 100, message = "Email must be up to 100 characters.")
    private String email;

    private boolean enabled;

    @NotNull(message = "Role id is required.")
    private Integer roleId;

    public UserEditDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotBlank @Size(min = 3, max = 50) String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank @Size(min = 3, max = 50) String username) {
        this.username = username;
    }

    public @NotBlank @Size(min = 6, max = 255) String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 6, max = 255) String password) {
        this.password = password;
    }

    public @NotBlank @Email @Size(max = 100) String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email @Size(max = 100) String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}