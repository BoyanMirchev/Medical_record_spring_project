package com.example.Medical_record_project_Final.data.service;

import com.example.Medical_record_project_Final.data.entity.User;

import java.util.List;

public interface UserService {

    User create(User user);

    User update(Integer id, User user);

    void deleteById(Integer id);

    User getById(Integer id);

    List<User> getAll();

    User getByUsername(String username);

    User getByEmail(String email);
}