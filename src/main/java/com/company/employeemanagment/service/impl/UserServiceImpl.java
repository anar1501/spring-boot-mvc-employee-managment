package com.company.employeemanagment.service.impl;

import com.company.employeemanagment.enums.UserStatusEnum;
import com.company.employeemanagment.model.Role;
import com.company.employeemanagment.model.User;
import com.company.employeemanagment.model.UserStatus;
import com.company.employeemanagment.repository.RoleRepository;
import com.company.employeemanagment.repository.UserRepository;
import com.company.employeemanagment.repository.UserStatusRepository;
import com.company.employeemanagment.service.UserService;
import com.company.employeemanagment.utils.ApplicationUtils;
import com.company.employeemanagment.utils.MessageUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.company.employeemanagment.enums.RoleEnums.ROLE_USER;
import static com.company.employeemanagment.enums.UserStatusEnum.UNCONFIRMED;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    final RoleRepository roleRepository;
    final UserStatusRepository userStatusRepository;
    final MessageUtils messageUtils;
    final UserRepository userRepository;
    @Value("${my.message.subject}")
    String messageSubject;
    @Value("${my.message.body}")
    String messageBody;
    @Override
    public User save(User user) {
        user.setExpiredDate(ApplicationUtils.prepareRegistrationExpirationDate());
        user.setActivationCode(ApplicationUtils.getRandomNumberString());
        Role role = roleRepository.findRoleByName(ROLE_USER.getRoleName());
        user.setRole(role);
        UserStatus userStatus = userStatusRepository.findUserStatusByStatusId(UNCONFIRMED.getStatusId());
        user.setStatus(userStatus);
        User savedUser = userRepository.save(user);
        messageUtils.sendAsync(savedUser.getEmail(),messageSubject,messageBody+"http://localhost:8080/register-confirm?code="+ savedUser.getActivationCode());
        return savedUser;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

}
