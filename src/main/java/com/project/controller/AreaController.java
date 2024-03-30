package com.project.controller;

import com.project.entity.*;
import com.project.entity.enums.UserRole;
import com.project.helper.CookieHelper;
import com.project.helper.NotiHelper;
import com.project.service.AreaService;
import com.project.service.ElevatorService;
import com.project.service.JwtTokenService;
import com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/area")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @Autowired
    private ElevatorService elevatorService;

    @Autowired
    private UserService userService;

    @Autowired
    private CookieHelper cookieHelper;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private NotiHelper notiHelper;

    @GetMapping("/{areaId}")
    public String getAreaById(@PathVariable Integer areaId, Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        Area area = areaService.getAreaById(areaId);
        List<Elevator> elevators = elevatorService.getElevatorsByAreaId(areaId);
        model.addAttribute("area", area);
        model.addAttribute("elevators", elevators);
        return "area/details";
    }

    @GetMapping("/{areaId}/add-elevator")
    public String showAddElevatorForm(@PathVariable Integer areaId, Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        Elevator elevator = new Elevator();
        model.addAttribute("elevator", elevator);
        return "area/add_elevator";
    }

    @PostMapping("/{areaId}/add-elevator")
    public String addElevatorToArea(@PathVariable Integer areaId,
                                    @ModelAttribute("elevator") Elevator elevator,
                                    RedirectAttributes redirectAttributes) {
        elevatorService.addElevator(elevator);
        return "redirect:/area/" + areaId;
    }

    @PostMapping("/{areaId}/delete")
    public String deleteArea(@PathVariable Integer areaId, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        int actor = Integer.valueOf(cookieHelper.getUserId(request));
        notiHelper.createNotiForAllManagers(actor, "deleted area with id: " + areaId);
        areaService.deleteArea(areaId);
        return "redirect:/area";
    }
    @GetMapping("/{areaId}/edit")
    public String showEditAreaForm(@PathVariable Integer areaId, Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        Area area = areaService.getAreaById(areaId);
        model.addAttribute("area", area);
        return "area/edit";
    }

    @PostMapping("/{areaId}/edit")
    public String updateArea(@PathVariable Integer areaId,
                             @ModelAttribute("area") Area area,
                             RedirectAttributes redirectAttributes, HttpServletRequest request) {
        int actor = Integer.valueOf(cookieHelper.getUserId(request));
        notiHelper.createNotiForAllManagers(actor,"updated area with id: " + areaId);

        try {
            area.setId(areaId);
            areaService.updateArea(area);
            redirectAttributes.addFlashAttribute("message", "Updated Successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/area/" + areaId + "/edit";
        }
        return "redirect:/area/" + areaId;
    }

}
