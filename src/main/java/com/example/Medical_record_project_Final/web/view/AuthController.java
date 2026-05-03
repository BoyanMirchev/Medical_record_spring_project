package com.example.Medical_record_project_Final.web.view;

import com.example.Medical_record_project_Final.data.entity.Role;
import com.example.Medical_record_project_Final.data.entity.User;
import com.example.Medical_record_project_Final.data.repo.RoleRepository;
import com.example.Medical_record_project_Final.data.service.UserService;
import com.example.Medical_record_project_Final.dto.LoginDto;
import com.example.Medical_record_project_Final.dto.UserRegisterDto;
import com.example.Medical_record_project_Final.exception.DuplicateEntityException;
import com.example.Medical_record_project_Final.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final RoleRepository roleRepository;
    private final UserService userService;

    public AuthController(RoleRepository roleRepository, UserService userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userRegisterDto", new UserRegisterDto());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerSubmit(@Valid @ModelAttribute("userRegisterDto") UserRegisterDto dto,
                                   BindingResult bindingResult,
                                   Model model) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            bindingResult.reject("passwordMismatch", "Passwords do not match.");
        }

        Role role = roleRepository.findById(dto.getRoleId()).orElse(null);
        if (role == null) {
            bindingResult.rejectValue("roleId", "invalidRole", "Please choose a valid role.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleRepository.findAll());
            return "auth/register";
        }

        User user = UserMapper.toRegisterEntity(dto, role);

        try {
            userService.create(user);
        } catch (DuplicateEntityException ex) {
            bindingResult.reject("duplicateUser", ex.getMessage());
            model.addAttribute("roles", roleRepository.findAll());
            return "auth/register";
        }

        return "redirect:/login?registered";
    }
}
