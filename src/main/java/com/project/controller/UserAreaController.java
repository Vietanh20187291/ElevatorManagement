package com.project.controller;

import com.project.entity.UserArea;
import com.project.entity.Area;
import com.project.entity.UserDTO;
import com.project.service.UserAreaService;
import com.project.service.AreaService;
import com.project.service.UserService;
import com.project.service.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/permission")
public class UserAreaController {

    @Autowired
    private UserAreaService userAreaService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @GetMapping
    public String managePermissions(Model model, HttpServletRequest request) {
        try {
            String token = jwtTokenService.getTokenFromRequest(request);
            int buildingId = jwtTokenService.getBuildingIdFromToken(token);

            // Lấy danh sách UserArea theo buildingId
            List<UserArea> userAreas = userAreaService.getUserAreaByBuildingId(buildingId);

            // Lấy danh sách khu vực theo buildingId
            List<Area> areas = areaService.getAreasByBuildingId(buildingId);

            // Lấy danh sách người dùng theo buildingId
            List<UserDTO> users = userService.getUsersByBuildingId(buildingId);

            // Đưa các danh sách vào model
            model.addAttribute("userAreas", userAreas);
            model.addAttribute("areas", areas);
            model.addAttribute("users", users);

            return "/userarea/permission";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("errorMessage", "An error occurred while processing your request.");
            return "error_page"; // Điều hướng đến trang lỗi
        }
    }


    @PostMapping("/permission/update")
    public String updatePermissions(@RequestParam("userAreas") List<UserArea> userAreas,
                                    RedirectAttributes redirectAttributes) {
        try {
            userAreaService.updateUserAreas(userAreas);
            redirectAttributes.addFlashAttribute("message", "Permissions updated successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while updating permissions.");
        }
        return "redirect:/user-area/permission";
    }

    @PostMapping("/add")
    public String addPermission(@RequestParam("userArea") UserArea userArea,
                                RedirectAttributes redirectAttributes) {
        try {
            userAreaService.addUserArea(userArea);
            redirectAttributes.addFlashAttribute("message", "Permission added successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while adding permission.");
        }
        return "redirect:/permission"; // Điều hướng đến trang quản lý permissions
    }

    @PostMapping("/delete")
    public String deletePermission(@RequestParam("userAreaId") int userAreaId,
                                   RedirectAttributes redirectAttributes) {
        try {
            userAreaService.deleteById(userAreaId);
            redirectAttributes.addFlashAttribute("message", "Permission deleted successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while deleting permission.");
        }
        return "redirect:/permission"; // Điều hướng đến trang quản lý permissions
    }
}
