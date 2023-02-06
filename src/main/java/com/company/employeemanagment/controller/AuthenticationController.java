package com.company.employeemanagment.controller;


import com.company.employeemanagment.dto.request.ChangePasswordRequestDto;
import com.company.employeemanagment.model.User;
import com.company.employeemanagment.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@SessionAttributes(value = "user")
public class AuthenticationController {

    final UserService userService;

    @GetMapping(value = "/homepage")
    public String home() {
        return "homepage";
    }

    @GetMapping(value = "/register")
    public String registerPage() {
        return "registerpage";
    }

    @PostMapping(value = "/register")
    public String registerUser(User user, RedirectAttributes redirectAttributes) {
        return userService.registerUser(user, redirectAttributes);
    }

    @GetMapping(value = "/register-confirm")
    public String registerConfirm(@RequestParam(value = "code") String code, RedirectAttributes redirectAttributes) {
        return userService.registerConfirm(code, redirectAttributes);
    }

    @GetMapping(value = "/error-info")
    public String errorPage() {
        return "errorinfopage";
    }


    @GetMapping(value = "/successful")
    public String successPage() {
        return "successinfopage";
    }

    @GetMapping(value = "/login")
    public String loginPage() {
        return "loginpage";
    }

    @PostMapping(value = "/login")
    public ModelAndView loginUser(User user, Model model) {
        return userService.loginUser(user, model);
    }

    @GetMapping(value = "/forget-password")
    public String forgetPasswordPage() {
        return "forgetpasswordpage";
    }

    @PostMapping(value = "/forget-password")
    public ModelAndView sendForgetPasswordActivationCodeToEmail(String email, Model model) {
        return userService.sendForgetPasswordActivationCodeToEmail(email, model);
    }

    @GetMapping(value = "/forget-password-confirm")
    public ModelAndView validateForgetPasswordActivationCodeAndPrepareNewPassword(@RequestParam(value = "code") String code, Model model) {
        return userService.validateForgetPasswordActivationCodeAndPrepareNewPassword(code, model);
    }

    @PostMapping(value = "/save-new-password")
    public ModelAndView saveNewUserPasswordThatForgotten(ChangePasswordRequestDto requestDto, Model model) {
        return userService.saveNewUserPasswordThatForgotten(requestDto, model);
    }

    @GetMapping(value = "/resend")
    public ModelAndView resendEmail(@RequestParam(value = "id") Long id) {
        return userService.resendEmail(id);
    }

}
