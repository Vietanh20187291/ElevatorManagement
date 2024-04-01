package com.project.controller;

import com.project.entity.*;
import com.project.entity.enums.UserRole;
import com.project.helper.CookieHelper;
import com.project.helper.NotiHelper;
import com.project.service.*;
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
    private BuildingService buildingService;

    @Autowired
    private CookieHelper cookieHelper;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private NotiHelper notiHelper;
    @Autowired
    private ProjectVersionService projectVersionService;

    @GetMapping("/{areaId}")
    public String getAreaById(@PathVariable Integer areaId, Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        Area area = areaService.getAreaById(areaId);
        List<Elevator> elevators = elevatorService.getElevatorsByAreaId(areaId);
        model.addAttribute("area", area);
        model.addAttribute("elevators", elevators);
        return "area/findById";
    }

    @GetMapping("/{areaId}/add-elevator")
    public String showAddElevatorForm(@PathVariable Integer areaId, Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        Elevator elevator = new Elevator();
        Area area = areaService.getAreaById(areaId);
        model.addAttribute("elevator", elevator);
        model.addAttribute("area", area);
        return "elevator/add";
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
        System.out.println("PV id"+projectVersionService.getProjectVersionById(1).getProject().getProjectId());
        System.out.println("building id"+area.getBuilding().getName());
        area.setBuilding(buildingService.getBuildingById(1));
        model.addAttribute("area", area);
        return "area/edit";
    }

    @PostMapping("/{areaId}/edit")
    public String updateArea(@PathVariable Integer areaId,
                             @ModelAttribute("area") Area area,
                             RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {

        int actor = Integer.valueOf(cookieHelper.getUserId(request));
        notiHelper.createNotiForAllManagers(actor, "updated area " + area.getName() + " with ID " + area.getId());

        String token = jwtTokenService.getTokenFromRequest(request);
        User user = jwtTokenService.getUserFromToken(token);
        String redirectLink = "";

        if (user.getRole().equals(UserRole.MANAGER)) {
            redirectLink = "redirect:/area/" + areaId;
        } else if (user.getRole().equals(UserRole.USER)) {
            redirectLink = "redirect:/area/user/" + user.getUserId();
        }

        try {
            area.setId(areaId);
            areaService.updateArea(area);
            redirectAttributes.addFlashAttribute("message", "Updated Successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
            return redirectLink;
        }
        return redirectLink;
    }

}