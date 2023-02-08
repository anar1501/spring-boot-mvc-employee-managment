package com.company.employeemanagment.controller;

import com.company.employeemanagment.dto.request.EmployeeRequestDto;
import com.company.employeemanagment.service.EmployeeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SessionAttributes(value = "user")
public class EmployeeController {

    final EmployeeService employeeService;

    @GetMapping(value = "/employee-list")
    public ModelAndView employeeListPage() {
        return employeeService.employeeListPage();
    }

    @GetMapping(value = "/insert")
    public String insertPage() {
        return "insertpage";
    }

    @PostMapping(value = "/employee-save")
    public String insertedEmployee(EmployeeRequestDto requestDto) {
        return employeeService.insertedEmployee(requestDto);
    }

    @GetMapping(value = "/edit-employee-page")
    public ModelAndView editEmployeeListPage() {
        return employeeService.editEmployeeListPage();
    }

    @GetMapping(value = "/edit-employee")
    public ModelAndView editEmployeePage(@RequestParam(value = "id") Long id) {
        return employeeService.editEmployeePage(id);
    }

    @PostMapping(value = "/edit-employee-save")
    public String updatedEmployee(EmployeeRequestDto employeeRequestDto) {
        return employeeService.updatedEmployee(employeeRequestDto);
    }

    @GetMapping(value = "/delete-employee-page")
    public ModelAndView deleteEmployeePage() {
        return employeeService.deleteEmployeePage();
    }

    @GetMapping(value = "/delete-employee")
    public ModelAndView deletedEmployee(@RequestParam(value = "id") Long id) {
        return employeeService.deletedEmployee(id);
    }

    @PostMapping(value = "/delete-employee-save")
    public String deletedEmployeeConfirm(@RequestParam(value = "id") Long id) {
        return employeeService.deletedEmployeeConfirm(id);
    }

}
