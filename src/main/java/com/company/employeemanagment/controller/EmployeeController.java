package com.company.employeemanagment.controller;

import com.company.employeemanagment.dto.request.EmployeeRequestDto;
import com.company.employeemanagment.enums.UserStatusEnum;
import com.company.employeemanagment.mapstruct.EmployeeMapper;
import com.company.employeemanagment.model.Employee;
import com.company.employeemanagment.repository.EmployeeRepository;
import com.company.employeemanagment.repository.UserStatusRepository;
import com.company.employeemanagment.service.EmployeeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeController {

    final EmployeeService employeeService;

    @GetMapping(value = "/employee-list")
    ModelAndView employeeListPage() {
       return employeeService.employeeListPage();
    }

    @GetMapping(value = "/insert")
    String insertPage() {
        return "insertpage";
    }

    @PostMapping(value = "/employee-save")
    String insertedEmployee(EmployeeRequestDto requestDto) {
       return employeeService.insertedEmployee(requestDto);
    }

}
