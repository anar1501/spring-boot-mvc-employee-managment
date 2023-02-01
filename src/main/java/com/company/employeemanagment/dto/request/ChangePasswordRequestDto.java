package com.company.employeemanagment.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequestDto {
    Long id;
    String newPassword;
    String passwordRepeat;
}
