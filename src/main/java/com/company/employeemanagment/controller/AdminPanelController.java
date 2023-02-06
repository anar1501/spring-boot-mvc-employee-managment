package com.company.employeemanagment.controller;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminPanelController {

    @GetMapping(value = "/admin-panel")
    ModelAndView adminPanelPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("adminpanelpage");
        return modelAndView;
    }
}
