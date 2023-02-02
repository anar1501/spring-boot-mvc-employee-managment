package com.company.employeemanagment.service;

import com.company.employeemanagment.dto.request.EmployeeRequestDto;
import org.springframework.web.servlet.ModelAndView;

public interface EmployeeService {
    ModelAndView employeeListPage();

    String insertedEmployee(EmployeeRequestDto requestDto);
}
