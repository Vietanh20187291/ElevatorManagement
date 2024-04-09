package com.project.controller;

import com.project.entity.EmployeeAnalysis;
import com.project.entity.JwtResponse;
import com.project.entity.User;
import com.project.entity.UserDTO;
import com.project.entity.enums.UserRole;
import com.project.repository.TaskRepository;
import com.project.service.*;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

import static org.jvnet.fastinfoset.EncodingAlgorithmIndexes.UUID;
@Component
@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    JwtTokenService jwtTokenService;
    @Autowired
    EmployeeAnalysisService employeeAnalysisService;
    @Autowired
    LineGraphEmployeeTaskAnalysisService lineGraphEmployeeTaskAnalysisService;

    @GetMapping
    public String getLoginPage() {
        return "login";
//        return "mqtt/main";
    }
    @GetMapping("/1")
    public String getLoginPage1() {
//        return "login";
//        return "mqtt/main";
        return "elevator/test";
    }

    @PostMapping
    public String login(@ModelAttribute User user, Model model, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            boolean isAuthenticated = userService.checkLogin(user);
            if (isAuthenticated) {

                User userDetail = userService.getUserByUsername(user.getUsername());
                String jwtToken = jwtTokenService.generateToken(userDetail.getUserId(), userDetail.getRole(),userDetail.getBuilding().getId());
                System.out.println("User "+userDetail.getUsername());
                System.out.println("Role " + userDetail.getRole());
                // Tạo cookie chứa token
                Cookie tokenCookie = new Cookie("jwtToken", jwtToken);
                tokenCookie.setPath("/");
                tokenCookie.setMaxAge(7 * 24 * 60 * 60); // Thời gian sống của cookie (7 ngày)
                tokenCookie.setHttpOnly(true);
                tokenCookie.setSecure(true); // Nếu trang web chạy trên HTTPS, hãy đặt giá trị true

                Cookie userCookie = new Cookie("userId", String.valueOf(userDetail.getUserId()));
                userCookie.setPath("/");
                userCookie.setMaxAge(7 * 24 * 60 * 60); // Thời gian sống của cookie (7 ngày)
                userCookie.setHttpOnly(true);
                userCookie.setSecure(true);

                Cookie roleCookie = new Cookie("role", String.valueOf(userDetail.getRole()));
                roleCookie.setPath("/");
                roleCookie.setMaxAge(7 * 24 * 60 * 60); // Thời gian sống của cookie (7 ngày)
                roleCookie.setHttpOnly(true);
                roleCookie.setSecure(true);

                response.addCookie(tokenCookie);
                response.addCookie(userCookie);
                response.addCookie(roleCookie);
                if (userDetail.getRole().equals(UserRole.ADMIN)) {
                    return "redirect:/admin/user";
                } else if (userDetail.getRole().equals(UserRole.MANAGER)) {
//                    return "redirect:/user";
                    return "redirect:/building/1";
                } else {
                    return "redirect:/version/user/" + userDetail.getUserId();
                }
            } else {
                model.addAttribute("message", "Invalid username or password");
                model.addAttribute("messageType", "error");
                return "login";
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}



