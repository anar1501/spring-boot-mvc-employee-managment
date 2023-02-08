package com.company.employeemanagment.service;

import com.company.employeemanagment.dto.request.EmployeeRequestDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.servlet.ModelAndView;

public interface EmployeeService {
    ModelAndView employeeListPage();
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_DIRECTOR')")
    String insertedEmployee(EmployeeRequestDto requestDto);
    ModelAndView editEmployeeListPage();
    ModelAndView editEmployeePage(Long id);
    String updatedEmployee(EmployeeRequestDto employeeRequestDto);
    ModelAndView deleteEmployeePage();
    ModelAndView deletedEmployee(Long id);
    String deletedEmployeeConfirm(Long id);
}
