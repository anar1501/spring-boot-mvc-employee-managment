package com.company.employeemanagment.service.impl;

import com.company.employeemanagment.dto.request.EmployeeRequestDto;
import com.company.employeemanagment.mapstruct.EmployeeMapper;
import com.company.employeemanagment.model.Employee;
import com.company.employeemanagment.repository.EmployeeRepository;
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
        employeeRepository.save(employee);
        return "redirect:/employee-list";
    }

    @Override
    public ModelAndView editEmployeeListPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("employees", employeeRepository.findAll());
        modelAndView.setViewName("editemployeepage");
        return modelAndView;
    }

    @Override
    public ModelAndView editEmployeePage(Long id) {
        ModelAndView modelAndView = new ModelAndView();
        Employee employee = employeeRepository.findById(id).get();
        modelAndView.addObject("employee", employee);
        return modelAndView;
    }

    @Override
    public String updatedEmployee(EmployeeRequestDto employeeRequestDto) {
        Employee employee = employeeRepository.findById(employeeRequestDto.getId()).get();
        employee.setName(employeeRequestDto.getFirstName());
        employee.setSurname(employeeRequestDto.getLastName());
        employee.setAge(employeeRequestDto.getAge());
        employee.setSalary(employeeRequestDto.getSalary());
        employeeRepository.save(employee);
        return "redirect:employee-list";
    }

    @Override
    public ModelAndView deleteEmployeePage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("employees", employeeRepository.findAll());
        modelAndView.setViewName("deleteemployeepage");
        return modelAndView;
    }

    @Override
    public ModelAndView deletedEmployee(Long id) {
        ModelAndView modelAndView = new ModelAndView();
        Employee employee = employeeRepository.findById(id).get();
        modelAndView.addObject("employee",employee);
        modelAndView.setViewName("delete-employee");
        return modelAndView;
    }

    @Override
    public String deletedEmployeeConfirm(Long id) {
        Employee employee=employeeRepository.findById(id).get();
        employeeRepository.delete(employee);
        return "redirect:employee-list";
    }
}
