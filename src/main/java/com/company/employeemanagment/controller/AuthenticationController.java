package com.company.employeemanagment.controller;


import com.company.employeemanagment.enums.UserStatusEnum;
import com.company.employeemanagment.model.User;
import com.company.employeemanagment.model.UserStatus;
import com.company.employeemanagment.repository.UserRepository;
import com.company.employeemanagment.repository.UserStatusRepository;
import com.company.employeemanagment.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.dom4j.rule.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthenticationController {

    final UserService userService;
    final UserRepository userRepository;
    final UserStatusRepository userStatusRepository;

    @GetMapping(value = "/homepage")
    String home() {
        return "homepage";
    }

    @GetMapping(value = "/register")
    String registerPage() {
        return "registerpage";
    }

    @PostMapping(value = "/register")
    String registerUser(@Valid User user, RedirectAttributes redirectAttributes) {
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
        } else {
            if (!user.getPassword().equals(userByEmail.getPassword())) {
                model.addAttribute("error", "Password is incorrect!");
                modelAndView.setViewName("loginpage");
            } else if (!userByEmail.getStatus().getStatusId().equals(UserStatusEnum.CONFIRMED.getStatusId())) {
                model.addAttribute("infoex", "Your account is not confirmed!");
                modelAndView.setViewName("errorinfopage");
            } else {
                modelAndView.addObject("user", user);
                modelAndView.setViewName("homepage");
            }
        }
        return modelAndView;
    }


}
