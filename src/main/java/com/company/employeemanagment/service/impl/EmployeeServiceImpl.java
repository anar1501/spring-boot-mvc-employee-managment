package com.company.employeemanagment.service.impl;

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
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeServiceImpl implements EmployeeService {
    final EmployeeRepository employeeRepository;
    final EmployeeMapper employeeMapper;
    final UserStatusRepository userStatusRepository;

    @Override
    public ModelAndView employeeListPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("employees", employeeRepository.findAll());
        modelAndView.setViewName("employee-list-page");
        return modelAndView;
    }

    @Override
    public String insertedEmployee(EmployeeRequestDto requestDto) {
        Employee employee = employeeMapper.map(requestDto);
        employee.setUserStatus(userStatusRepository.findUserStatusByStatusId(UserStatusEnum.CONFIRMED.getStatusId()));
        employeeRepository.save(employee);
        return "redirect:/employee-list";
    }
}
