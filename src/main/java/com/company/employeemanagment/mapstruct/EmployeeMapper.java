package com.company.employeemanagment.mapstruct;

import com.company.employeemanagment.dto.request.EmployeeRequestDto;
import com.company.employeemanagment.model.Employee;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Objects;

@Mapper(componentModel = "spring",builder = @Builder(disableBuilder = true),imports = {Objects.class})
public interface EmployeeMapper {

    @Mapping(target = "name",source = "firstName")
    @Mapping(target = "surname",source = "lastName")
    @Mapping(target = "employeeId",ignore = true)
    @Mapping(target = "createdDate",ignore = true)
    @Mapping(target = "updateDate",ignore = true)
    Employee map(EmployeeRequestDto requestDto);
}
