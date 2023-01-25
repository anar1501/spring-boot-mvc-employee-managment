package com.company.employeemanagment.controller;


import com.company.employeemanagment.model.User;
import com.company.employeemanagment.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthenticationController {

    final UserService userService;

    @GetMapping(value = "/homepage")
    String home(){
        return "homepage";
    }

    @GetMapping(value = "/register")
    String registerPage(){
        return "registerpage";
    }

    @PostMapping(value = "/register")
    String registerUser(@Valid User user, RedirectAttributes redirectAttributes){
        User userByEmail = userService.findUserByEmail(user.getEmail());
        if (userByEmail!=null){
            redirectAttributes.addFlashAttribute("errore","There is already a email registered with the email provided");
            return "redirect:register";
        }else if (!user.getPassword().equals(user.getRepeatPassword())){
            redirectAttributes.addFlashAttribute("errorp","Password repeat is not same!");
            return "redirect:register";
        }
        userService.save(user);
        redirectAttributes.addFlashAttribute("infos","Your registration was successfully! Pls check your email.");
        return "redirect:successful";
    }

    @GetMapping(value = "/successful")
    String successPage() {
        return "successinfopage";
    }

}
