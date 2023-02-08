package com.company.employeemanagment.service.impl;

import com.company.employeemanagment.dto.request.ChangePasswordRequestDto;
import com.company.employeemanagment.enums.UserStatusEnum;
import com.company.employeemanagment.model.Role;
import com.company.employeemanagment.model.User;
import com.company.employeemanagment.model.UserStatus;
import com.company.employeemanagment.repository.RoleRepository;
import com.company.employeemanagment.repository.UserRepository;
import com.company.employeemanagment.repository.UserStatusRepository;
import com.company.employeemanagment.service.UserService;
import com.company.employeemanagment.utils.DateTimeUtils;
import com.company.employeemanagment.utils.MessageUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static com.company.employeemanagment.enums.RoleEnums.ROLE_USER;
import static com.company.employeemanagment.enums.UserStatusEnum.UNCONFIRMED;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    final RoleRepository roleRepository;
    final UserStatusRepository userStatusRepository;
    final MessageUtils messageUtils;
    final UserRepository userRepository;
    @Value("${my.message.subject}")
    String messageSubject;
    @Value("${my.message.body}")
    String messageBody;
    @Value("${my.message.forget-subject}")
    String forgetMessageSubject;
    @Value("${my.message.forget-body}")
    String forgetMessageBody;
    final PasswordEncoder passwordEncoder;

    @Override
    public User save(User user) {
        user.setExpiredDate(DateTimeUtils.prepareRegistrationExpirationDate());
        user.setActivationCode(DateTimeUtils.getRandomNumberString());
        Role role = roleRepository.findRoleByName(ROLE_USER.getRoleName());
        user.setRole(role);
        UserStatus userStatus = userStatusRepository.findUserStatusByStatusId(UNCONFIRMED.getStatusId());
        user.setStatus(userStatus);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        messageUtils.sendAsync(savedUser.getEmail(), messageSubject, messageBody + "http://localhost:8080/register-confirm?code=" + savedUser.getActivationCode());
        return savedUser;
    }

    @Override
    public String registerUser(User user, RedirectAttributes redirectAttributes, Model model) {
        User userByEmail = userRepository.findUserByEmail(user.getEmail());
        if (userByEmail != null) {
            redirectAttributes.addFlashAttribute("errore", "There is already a email registered with the email provided");
            model.addAttribute("user", null);
            return "redirect:/register";
        } else if (!user.getPassword().equals(user.getRepeatPassword())) {
            redirectAttributes.addFlashAttribute("errorp", "Password repeat is not same!");
            model.addAttribute("user", null);
            return "redirect:/register";
        }
        this.save(user);
        redirectAttributes.addFlashAttribute("infos", "Your registration was successfully! Pls check your email.");
        model.addAttribute("user", null);
        return "redirect:/successful";
    }

    @Override
    public String registerConfirm(String code, RedirectAttributes redirectAttributes) {
        if (Objects.isNull(code)) {
            redirectAttributes.addFlashAttribute("infoe", "Confirmation code is not correct!");
            return "redirect:/error-info";
        } else {
            User user = userRepository.findUserByActivationCode(code);
            if (Objects.isNull(user)) {
                redirectAttributes.addFlashAttribute("infoe", "Confirmation code is not correct!");
                return "redirect:/error-info";
            } else {
                Date expiredDate = user.getExpiredDate();
                Date currentDate = new Date();
                if (expiredDate.before(currentDate)) {
                    redirectAttributes.addFlashAttribute("infoex", "Confirmation code is expired!");
                    redirectAttributes.addFlashAttribute("infos", "/resend?id=" + user.getId());
                    return "redirect:/error-info";
                } else if (Objects.equals(user.getStatus().getStatusId(), UserStatusEnum.CONFIRMED.getStatusId())) {
                    redirectAttributes.addFlashAttribute("infos", "Your account is already confirmed!");
                    return "redirect:/error-info";
                } else {
                    user.setStatus(userStatusRepository.findUserStatusByStatusId(UserStatusEnum.CONFIRMED.getStatusId()));
                    userRepository.save(user);
                    redirectAttributes.addFlashAttribute("infos", "Your account is successfully confirmed!");
                    return "redirect:/successful";
                }
            }
        }
    }

    @Override
    public ModelAndView loginUser(User user, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        User userByEmail = userRepository.findUserByEmail(user.getEmail());
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
                modelAndView.addObject("infos", "/resend?id=" + userByEmail.getId());
                modelAndView.setViewName("errorinfopage");
            } else {
                model.addAttribute("user", userByEmail);
                modelAndView.addObject("user", userByEmail);
                modelAndView.setViewName("homepage");
            }
        }
        return modelAndView;
    }

    @Override
    public ModelAndView sendForgetPasswordActivationCodeToEmail(String email, Model model) {
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
                user.setForgetPasswordExpiredDate(DateTimeUtils.prepareForgetPasswordExpirationDate());
                User save = userRepository.save(user);
                messageUtils.sendAsync(save.getEmail(), forgetMessageSubject, forgetMessageBody + "http://localhost:8080/forget-password-confirm?code=" + save.getPasswordActivationCode());
                model.addAttribute("infos", "Password Confirmation code was successfully sent! Please check your email!");
                modelAndView.setViewName("successinfopage");
            }
        }
        return modelAndView;
    }

    @Override
    public ModelAndView validateForgetPasswordActivationCodeAndPrepareNewPassword(String code, Model model) {
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

    @Override
    public ModelAndView saveNewUserPasswordThatForgotten(Long id, ChangePasswordRequestDto requestDto, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        if (Objects.isNull(requestDto) || !requestDto.getNewPassword().equals(requestDto.getPasswordRepeat())) {
            model.addAttribute("error", "Password is not same");
            modelAndView.setViewName("changepasswordpage");
            model.addAttribute("id", id);
        } else {
            User user = userRepository.findById(id).get();
            user.setId(id);
            user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
            userRepository.save(user);
            model.addAttribute("infos", "Password successfully changed!");
            modelAndView.setViewName("successinfopage");
        }
        return modelAndView;
    }

    @Override
    public ModelAndView resendEmail(Long id) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userRepository.findById(id).get();
        this.save(user);
        modelAndView.addObject("infos", "Activation code was again successfully sent! Please check your email!");
        modelAndView.setViewName("successinfopage");
        return modelAndView;
    }

}
