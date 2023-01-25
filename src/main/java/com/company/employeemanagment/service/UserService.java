package com.company.employeemanagment.service;

import com.company.employeemanagment.model.User;

public interface UserService {
    User save(User user);

    User findUserByEmail(String email);
}
