package com.project.controller;

import com.project.entity.JwtResponse;
import com.project.entity.User;
import com.project.entity.UserDTO;
import com.project.entity.enums.UserRole;
import com.project.helper.CookieHelper;
import com.project.service.JwtTokenService;
import com.project.service.UserService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static org.jvnet.fastinfoset.EncodingAlgorithmIndexes.UUID;
@Component
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    JwtTokenService jwtTokenService;
    @Autowired
    private CookieHelper cookieHelper;

    @GetMapping("/add")
    public String addUser(Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        User userRequest = new User();
        model.addAttribute("userRequest", userRequest);
        return "user/add";
    }

    @PostMapping("/add")
    public String addUser(HttpServletRequest request, @ModelAttribute("userRequest") User userRequest, RedirectAttributes redirectAttributes) {
        try {
            if(userRequest.getRole() == UserRole.ADMIN){
                throw new Exception("You do not right to create Admin account");
            }
            User user = userService.addUser(userRequest.getUsername(), userRequest.getPassword(), userRequest.getRole());    redirectAttributes.addFlashAttribute("message", "User has been added successfully");
            redirectAttributes.addFlashAttribute("message", "Added successfully");
            redirectAttributes.addFlashAttribute("messageType","success");
            return "redirect:/user";
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            redirectAttributes.addFlashAttribute("message", errorMessage);
            return "redirect:/user";
        }
    }

    @PostMapping("/update-role/{id}")
    public String updateUserRole(HttpServletRequest request, @PathVariable Integer id,
                                 @RequestParam("newRole") UserRole newRole,
                                 RedirectAttributes redirectAttributes) {
        try {
            if(newRole == UserRole.ADMIN){
                throw new Exception("Access Denied");
            }
            User updatedUser = userService.updateUserRole(id, newRole);
            redirectAttributes.addFlashAttribute("message", "User role has been updated successfully");
            redirectAttributes.addFlashAttribute("messageType","success");
            return "redirect:/user";
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            redirectAttributes.addFlashAttribute("message", errorMessage);
            redirectAttributes.addFlashAttribute("messageType","error");
            return "redirect:/user";
        }
    }
    @GetMapping("/password")
    public String showChangePasswordForm(Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        model.addAttribute("password");
        return "user/password";
    }
    @PostMapping("/password")
    public String updateUserPassword(@RequestParam("oldPass") String oldPass,
                                                @RequestParam("newPass") String newPass,
                                     RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
    String token = jwtTokenService.getTokenFromRequest(request);
    User user = jwtTokenService.getUserFromToken(token);
    user.setPassword(oldPass);
        boolean isAuthenticated = userService.checkLogin(user);
        if (isAuthenticated) {
            // Gọi hàm updatePassword với id là 1 và password mới
            int id = user.getUserId();
            User updatedUser = userService.updatePassword(id, newPass);
            redirectAttributes.addFlashAttribute("message", "Update successful!");
            redirectAttributes.addFlashAttribute("messageType","success");
        } else {
            redirectAttributes.addFlashAttribute("message", "Current Password is wrong!");
            redirectAttributes.addFlashAttribute("messageType","error");
        }
        //        try {
//            // Lấy người dùng hiện tại từ token
//            User currentUser = jwtTokenService.getUserFromToken(request);
//
//            // Cập nhật mật khẩu của người dùng
//            User updatedUser = userService.updatePassword(currentUser.getUserId(), password);
//
//            return ResponseEntity.ok(updatedUser);
//        } catch (Exception e) {
//            String errorMessage = e.getMessage();
//            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
        return "redirect:/user/password";
    }
//    @GetMapping
//    public String getAllUsers(Model model, HttpServletRequest request) {
//        cookieHelper.addCookieAttributes(request, model);
//        List<UserDTO> users = userService.getAllUsers();
//        model.addAttribute("users", users);
//        return "user/users";
//    }

    @GetMapping
    public String getAllUsers(Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        List<UserDTO> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user/users";
    }
    @PostMapping("/deactivate/{id}")
    public String deleteUser(HttpServletRequest request, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
        System.out.println("delete");
        try {
            userService.deactivateUser(id);
            redirectAttributes.addFlashAttribute("message", "User has been deactivated successfully");
            redirectAttributes.addFlashAttribute("messageType","success");
            return "redirect:/user";
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            redirectAttributes.addFlashAttribute("message", errorMessage);
            redirectAttributes.addFlashAttribute("messageType","success");
            return "redirect:/user";
        }
    }


}
