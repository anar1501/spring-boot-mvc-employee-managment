package com.company.employeemanagment.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeRequestDto {
    Long id;
    String firstName;
    String lastName;
    int age;
    BigDecimal salary;
}
