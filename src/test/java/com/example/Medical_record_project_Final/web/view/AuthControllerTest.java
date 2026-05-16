package com.example.Medical_record_project_Final.web.view;

import com.example.Medical_record_project_Final.data.entity.Role;
import com.example.Medical_record_project_Final.data.repo.RoleRepository;
import com.example.Medical_record_project_Final.data.service.UserService;
import com.example.Medical_record_project_Final.exception.DuplicateEntityException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoleRepository roleRepository;

    @MockitoBean
    private UserService userService;

    @Test
    void loginShouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"))
                .andExpect(model().attributeExists("loginDto"));
    }

    @Test
    void registerShouldReturnRegisterViewWithRoles() throws Exception {
        Role role = new Role();
        role.setId(1);
        role.setRoleName("ADMIN");

        when(roleRepository.findAll()).thenReturn(List.of(role));

        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeExists("userRegisterDto"))
                .andExpect(model().attributeExists("roles"));
    }

    @Test
    void registerSubmitShouldRedirectToLoginWhenDataIsValid() throws Exception {
        Role role = new Role();
        role.setId(1);
        role.setRoleName("ADMIN");

        when(roleRepository.findById(1)).thenReturn(Optional.of(role));

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "admin")
                        .param("email", "admin@test.com")
                        .param("password", "password123")
                        .param("confirmPassword", "password123")
                        .param("roleId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"));

        verify(userService, times(1)).create(any());
    }

    @Test
    void registerSubmitShouldReturnRegisterViewWhenPasswordsDoNotMatch() throws Exception {
        Role role = new Role();
        role.setId(1);
        role.setRoleName("ADMIN");

        when(roleRepository.findById(1)).thenReturn(Optional.of(role));
        when(roleRepository.findAll()).thenReturn(List.of(role));

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "admin")
                        .param("email", "admin@test.com")
                        .param("password", "password123")
                        .param("confirmPassword", "differentPassword")
                        .param("roleId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeHasErrors("userRegisterDto"))
                .andExpect(model().attributeExists("roles"));

        verify(userService, never()).create(any());
    }

    @Test
    void registerSubmitShouldReturnRegisterViewWhenRoleIsInvalid() throws Exception {
        when(roleRepository.findById(99)).thenReturn(Optional.empty());
        when(roleRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "admin")
                        .param("email", "admin@test.com")
                        .param("password", "password123")
                        .param("confirmPassword", "password123")
                        .param("roleId", "99"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeHasFieldErrors("userRegisterDto", "roleId"))
                .andExpect(model().attributeExists("roles"));

        verify(userService, never()).create(any());
    }

    @Test
    void registerSubmitShouldReturnRegisterViewWhenUsernameIsBlank() throws Exception {
        when(roleRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "")
                        .param("email", "admin@test.com")
                        .param("password", "password123")
                        .param("confirmPassword", "password123")
                        .param("roleId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeHasFieldErrors("userRegisterDto", "username"));

        verify(userService, never()).create(any());
    }

    @Test
    void registerSubmitShouldReturnRegisterViewWhenUserAlreadyExists() throws Exception {
        Role role = new Role();
        role.setId(1);
        role.setRoleName("ADMIN");

        when(roleRepository.findById(1)).thenReturn(Optional.of(role));
        when(roleRepository.findAll()).thenReturn(List.of(role));

        doThrow(new DuplicateEntityException("User already exists."))
                .when(userService).create(any());

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "admin")
                        .param("email", "admin@test.com")
                        .param("password", "password123")
                        .param("confirmPassword", "password123")
                        .param("roleId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeHasErrors("userRegisterDto"))
                .andExpect(model().attributeExists("roles"));

        verify(userService, times(1)).create(any());
    }
}