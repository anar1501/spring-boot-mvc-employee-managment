package com.company.employeemanagment.repository;

import com.company.employeemanagment.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {
    UserStatus findUserStatusByStatusId(Long id);
}

