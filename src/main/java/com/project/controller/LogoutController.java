package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LogoutController {

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Lấy danh sách cookie từ request
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwtToken")) {
                    // Xóa cookie
                    cookie.setValue(""); // Thiết lập giá trị cookie thành rỗng
                    cookie.setMaxAge(0); // Thiết lập thời gian sống của cookie thành 0
                    cookie.setPath("/");
                    cookie.setHttpOnly(true);
                    cookie.setSecure(true); // Nếu trang web chạy trên HTTPS, đặt giá trị true
                    response.addCookie(cookie);
                    break;
                }
            }
        }

        // Chuyển hướng đến "/login"
        return "redirect:/login";
    }
}
