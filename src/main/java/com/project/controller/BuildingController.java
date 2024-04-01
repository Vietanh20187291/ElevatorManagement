package com.project.controller;

import com.project.entity.Area;
import com.project.entity.Building;
import com.project.service.AreaService;
import com.project.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/building")
public class BuildingController {
    @Autowired
    private BuildingService buildingService;

    @Autowired
    private AreaService areaService;


    @GetMapping("/name/{name}")
    public String getBuildingByName(@PathVariable String name, Model model, HttpServletRequest request) {
        Optional<Building> building = buildingService.getBuildingByName(name);
        if (building.isPresent()) {
            model.addAttribute("building", building.get());
            return "building/detail";
        } else {
            // Xử lý trường hợp không tìm thấy toà nhà có tên được cung cấp
            return "building/not_found";
        }
    }

    @GetMapping("/{buildingId}/edit")
    public String showEditBuildingForm(@PathVariable Integer buildingId, Model model, HttpServletRequest request) {
        Building building = buildingService.getBuildingById(buildingId);
        model.addAttribute("building", building);
        return "building/edit";
    }

    @PostMapping("/{buildingId}/edit")
    public String updateBuilding(@PathVariable Integer buildingId,
                                 @ModelAttribute("building") Building building,
                                 HttpServletRequest request) {
        building.setId(buildingId);
        buildingService.updateBuilding(building);
        return "redirect:/building/" + buildingId;
    }

    @PostMapping("/{buildingId}/delete")
    public String deleteBuilding(@PathVariable Integer buildingId, HttpServletRequest request) {
        buildingService.deleteBuilding(buildingId);
        return "redirect:/building/list"; // Điều hướng đến trang danh sách toà nhà sau khi xóa toà nhà
    }

    @GetMapping("/{buildingId}/add-area")
    public String showAddAreaForm(@PathVariable Integer buildingId, Model model, HttpServletRequest request) {
        Building building = buildingService.getBuildingById(buildingId);
        model.addAttribute("building", building);
        Area area = new Area();
        // Đặt toà nhà cho khu vực mới được tạo
        area.setBuilding(building);
        model.addAttribute("area", area);

        return "area/add";
    }

    @PostMapping("/{buildingId}/add-area")
    public String addAreaToBuilding(@PathVariable Integer buildingId,
                                    @ModelAttribute("area") Area area,
                                    RedirectAttributes redirectAttributes,
                                    HttpServletRequest request) {
        Building building = buildingService.getBuildingById(buildingId);

        // Kiểm tra xem toà nhà và khu vực có tồn tại không
        if (building != null) {
            try {
                // Đặt lại toà nhà cho khu vực (nếu không sử dụng đặt toà nhà trong phương thức GET)
                area.setBuilding(building);
                // Thêm khu vực vào toà nhà
                areaService.addArea(area);
                // Cập nhật thông tin của toà nhà trong cơ sở dữ liệu
                buildingService.updateBuilding(building);

                // Điều hướng đến trang chi tiết của toà nhà
                return "redirect:/building/" + buildingId;
            } catch (Exception e) {
                // Xử lý ngoại lệ nếu có
                redirectAttributes.addFlashAttribute("message", e.getMessage());
                redirectAttributes.addFlashAttribute("messageType", "error");
            }
        } else {
            // Xử lý trường hợp không tìm thấy toà nhà
            redirectAttributes.addFlashAttribute("message", "Building not found");
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        // Điều hướng trở lại trang thêm khu vực nếu có lỗi xảy ra
        return "redirect:/building/" + buildingId + "/add-area";
    }

    @GetMapping("/{buildingId}")
    public String showAreasByBuildingId(@PathVariable Integer buildingId, Model model) {
        Building building = buildingService.getBuildingById(buildingId);
        List<Area> areas = areaService.getAreasByBuildingId(buildingId);
        model.addAttribute("building", building);
        model.addAttribute("areas", areas);
        return "building/areas";
    }


}

