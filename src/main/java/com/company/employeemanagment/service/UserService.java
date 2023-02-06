package com.company.employeemanagment.service;

import com.company.employeemanagment.dto.request.ChangePasswordRequestDto;
import com.company.employeemanagment.model.User;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface UserService {
    User save(User user);

    User findUserByEmail(String email);

    String registerUser(User user, RedirectAttributes redirectAttributes);

    String registerConfirm(String code, RedirectAttributes redirectAttributes);

    ModelAndView loginUser(User user, Model model);

    ModelAndView sendForgetPasswordActivationCodeToEmail(String email, Model model);

    ModelAndView validateForgetPasswordActivationCodeAndPrepareNewPassword(String code, Model model);

    ModelAndView saveNewUserPasswordThatForgotten(ChangePasswordRequestDto requestDto, Model model);

    ModelAndView resendEmail(Long id);
}
