package com.company.employeemanagment.repository;

import com.company.employeemanagment.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {}
