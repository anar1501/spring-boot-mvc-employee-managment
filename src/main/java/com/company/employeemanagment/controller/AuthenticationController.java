package com.company.employeemanagment.controller;


import com.company.employeemanagment.dto.request.ChangePasswordRequestDto;
import com.company.employeemanagment.enums.UserStatusEnum;
import com.company.employeemanagment.model.User;
import com.company.employeemanagment.repository.UserRepository;
import com.company.employeemanagment.repository.UserStatusRepository;
import com.company.employeemanagment.service.UserService;
import com.company.employeemanagment.utils.ApplicationUtils;
import com.company.employeemanagment.utils.MessageUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthenticationController {

    final UserService userService;
    final UserRepository userRepository;
    final UserStatusRepository userStatusRepository;
    final PasswordEncoder passwordEncoder;
    final MessageUtils messageUtils;
    @Value("${my.message.forget-subject}")
    String forgetMessageSubject;
    @Value("${my.message.forget-body}")
    String forgetMessageBody;

    @GetMapping(value = "/homepage")
    String home() {
        return "homepage";
    }

    @GetMapping(value = "/register")
    String registerPage() {
        return "registerpage";
    }

    @PostMapping(value = "/register")
    String registerUser(User user, RedirectAttributes redirectAttributes) {
        User userByEmail = userService.findUserByEmail(user.getEmail());
        if (userByEmail != null) {
            redirectAttributes.addFlashAttribute("errore", "There is already a email registered with the email provided");
            return "redirect:register";
        } else if (!user.getPassword().equals(user.getRepeatPassword())) {
            redirectAttributes.addFlashAttribute("errorp", "Password repeat is not same!");
            return "redirect:register";
        }
        userService.save(user);
        redirectAttributes.addFlashAttribute("infos", "Your registration was successfully! Pls check your email.");
        return "redirect:successful";
    }

    @GetMapping(value = "/register-confirm")
    String registerConfirm(@RequestParam(value = "code") String code, RedirectAttributes redirectAttributes) {
        if (Objects.isNull(code)) {
            redirectAttributes.addFlashAttribute("infoe", "Confirmation code is not correct!");
            return "redirect:error-info";
        } else {
            User user = userRepository.findUserByActivationCode(code);
            if (Objects.isNull(user)) {
                redirectAttributes.addFlashAttribute("infoe", "Confirmation code is not correct!");
                return "redirect:error-info";
            } else {
                Date expiredDate = user.getExpiredDate();
                Date currentDate = new Date();
                if (expiredDate.before(currentDate)) {
                    redirectAttributes.addFlashAttribute("infoex", "Confirmation code is expired!");
                    redirectAttributes.addFlashAttribute("infos", "/resend?id=" + user.getId());
                    return "redirect:error-info";
                } else if (Objects.equals(user.getStatus().getStatusId(), UserStatusEnum.CONFIRMED.getStatusId())) {
                    redirectAttributes.addFlashAttribute("infos", "Your account is already confirmed!");
                    return "redirect:error-info";
                } else {
                    user.setStatus(userStatusRepository.findUserStatusByStatusId(UserStatusEnum.CONFIRMED.getStatusId()));
                    userRepository.save(user);
                    redirectAttributes.addFlashAttribute("infos", "Your account is successfully confirmed!");
                    return "redirect:successful";
                }
            }
        }
    }

    @GetMapping(value = "/error-info")
    String errorPage() {
        return "errorinfopage";
    }


    @GetMapping(value = "/successful")
    String successPage() {
        return "successinfopage";
    }

    @GetMapping(value = "/login")
    String loginPage() {
        return "loginpage";
    }

    @PostMapping(value = "/login")
    ModelAndView loginUser(User user, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        User userByEmail = userService.findUserByEmail(user.getEmail());
        if (Objects.isNull(userByEmail)) {
            model.addAttribute("error", "Email is incorrect!");
            modelAndView.setViewName("loginpage");
            model.addAttribute("user", null);
        } else {
            boolean matches = passwordEncoder.matches(user.getPassword(), userByEmail.getPassword());
            if (!matches) {
                model.addAttribute("error", "Password is incorrect!");
                modelAndView.setViewName("loginpage");
                model.addAttribute("user", null);
            } else if (!userByEmail.getStatus().getStatusId().equals(UserStatusEnum.CONFIRMED.getStatusId())) {
                model.addAttribute("infoex", "Your account is not confirmed!");
                modelAndView.setViewName("errorinfopage");
                model.addAttribute("user", null);
            } else {
                model.addAttribute("user", userByEmail);
                modelAndView.setViewName("homepage");
            }
        }
        return modelAndView;
    }

    @GetMapping(value = "/forget-password")
    String forgetPasswordPage() {
        return "forgetpasswordpage";
    }

    @PostMapping(value = "/forget-password")
    ModelAndView sendForgetPasswordActivationCodeToEmail(String email, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userRepository.findUserByEmail(email);
        if (Objects.isNull(email) || !email.contains("@")) {
            model.addAttribute("error", "No user found for such e-mail.");
            modelAndView.setViewName("forgetpasswordpage");
        } else {
            if (Objects.isNull(user)) {
                model.addAttribute("error", "No user found for such e-mail.");
                modelAndView.setViewName("forgetpasswordpage");
            } else {
                user.setPasswordActivationCode(passwordEncoder.encode(UUID.randomUUID().toString()));
                user.setForgetPasswordExpiredDate(ApplicationUtils.prepareForgetPasswordExpirationDate());
                User save = userRepository.save(user);
                messageUtils.sendAsync(save.getEmail(), forgetMessageSubject, forgetMessageBody + "http://localhost:8080/forget-password-confirm?code=" + save.getPasswordActivationCode());
                model.addAttribute("infos", "Password Confirmation code was successfully sent! Please check your email!");
                modelAndView.setViewName("successinfopage");
            }
        }
        return modelAndView;
    }

    @GetMapping(value = "/forget-password-confirm")
    ModelAndView validateForgetPasswordActivationCodeAndPrepareNewPassword(@RequestParam(value = "code") String code, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        if (Objects.isNull(code)) {
            model.addAttribute("infoe", "Forget Password Confirmation code is not correct!");
            modelAndView.setViewName("errorinfopage");
        } else {
            User user = userRepository.findUserByPasswordActivationCode(code);
            if (!user.getPasswordActivationCode().equals(code)) {
                model.addAttribute("infoe", "Forget Password Confirmation code is not correct!");
                modelAndView.setViewName("errorinfopage");
            } else {
                Date forgetPasswordExpiredDate = user.getForgetPasswordExpiredDate();
                Date currentDate = new Date();
                if (forgetPasswordExpiredDate.before(currentDate)) {
                    model.addAttribute("infoex", "Forget Password Confirmation code is expired!");
                    model.addAttribute("infos", "/resend?id=" + user.getId());
                    modelAndView.setViewName("errorinfopage");
                } else {
                    model.addAttribute("id", user.getId());
                    modelAndView.setViewName("changepasswordpage");
                }
            }
        }
        return modelAndView;
    }

    @PostMapping(value = "/save-new-password")
    ModelAndView saveNewUserPasswordThatForgotten(ChangePasswordRequestDto requestDto, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        if (Objects.isNull(requestDto) || !requestDto.getNewPassword().equals(requestDto.getPasswordRepeat())) {
            model.addAttribute("error", "Password is not same");
            modelAndView.setViewName("changepasswordpage");
        } else {
            User user = userRepository.findById(requestDto.getId()).get();
            user.setId(requestDto.getId());
            user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
            userRepository.save(user);
            model.addAttribute("infos", "Password successfully changed!");
            modelAndView.setViewName("successinfopage");
        }
        return modelAndView;
    }

}
