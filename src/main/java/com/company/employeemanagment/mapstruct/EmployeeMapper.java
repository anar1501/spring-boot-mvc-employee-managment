package com.company.employeemanagment.mapstruct;

import com.company.employeemanagment.dto.request.EmployeeRequestDto;
import com.company.employeemanagment.enums.UserStatusEnum;
import com.company.employeemanagment.model.Employee;
import com.company.employeemanagment.repository.UserStatusRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), imports = {Objects.class})
public abstract class EmployeeMapper {

    @Autowired
    protected UserStatusRepository userStatusRepository;


    @Mapping(target = "name", source = "firstName")
    @Mapping(target = "surname", source = "lastName")
    @Mapping(target = "employeeId", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    public abstract Employee map(EmployeeRequestDto requestDto);

    @AfterMapping
    void map(@MappingTarget Employee target) {
        target.setUserStatus(userStatusRepository.findUserStatusByStatusId(UserStatusEnum.CONFIRMED.getStatusId()));
    }


}
