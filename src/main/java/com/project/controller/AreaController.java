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
    @Autowired
    private MqttConnectionService mqttConnectionService;
    @Autowired
    private FloorService floorService;

    @GetMapping("/{areaId}")
    public String getAreaById(@PathVariable Integer areaId, Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        Area area = areaService.getAreaById(areaId);
        List<Elevator> elevators = elevatorService.getElevatorsByAreaId(areaId);
        model.addAttribute("area", area);
        model.addAttribute("elevators", elevators);
        MqttConnection mqttConnection = mqttConnectionService.getMqttConnection();
        model.addAttribute("mqttConnection", mqttConnection);
        return "area/findById";
    }
//    @GetMapping("/{areaId}/elevator-simulation")
//    public String elevatorSimulation(@PathVariable Integer areaId, Model model, HttpServletRequest request) {
//        cookieHelper.addCookieAttributes(request, model);
//        Area area = areaService.getAreaById(areaId);
//        List<Elevator> elevators = elevatorService.getElevatorsByAreaId(areaId);
//        model.addAttribute("area", area);
//        model.addAttribute("elevators", elevators);
//        model.addAttribute("numFloors",elevators.get(0).getNumFloors());
//        System.out.println("numFloors: "+elevators.get(0).getNumFloors());
//        model.addAttribute("numElevators",elevators.size());
//        System.out.println("numElevators: "+elevators.size());
//        MqttConnection mqttConnection = mqttConnectionService.getMqttConnection();
//        model.addAttribute("mqttConnection", mqttConnection);
//        List<Floor> floors = floorService.getAllFloorsByElevatorId(1);
//        model.addAttribute("floors", floors);
//        return "elevator/simulation";
//    }

    @GetMapping("/{areaId}/add-elevator")
    public String showAddElevatorForm(@PathVariable Integer areaId, Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        Area area = areaService.getAreaById(areaId);
        Building building = buildingService.getBuildingByAreaId(areaId);
        model.addAttribute("building", building);
        model.addAttribute("area", area);
        model.addAttribute("elevator", new Elevator());

        return "elevator/add";
    }

    @PostMapping("/{areaId}/add-elevator")
    public String addElevatorToArea(@PathVariable Integer areaId,
                                    @ModelAttribute("elevator") Elevator elevator,
                                    RedirectAttributes redirectAttributes) {
        int numFloorsCount = countFloors(elevator.getListFloors());
        elevator.setNumFloors(numFloorsCount);
        System.out.println(elevator.toString());
        int elevator_id = elevatorService.addElevator(elevator);
        System.out.println("elevator_id: "+elevator_id);
        return "redirect:/area/" + areaId;
    }

    private int countFloors(String listFloors) {
        if (listFloors == null || listFloors.isEmpty() || listFloors == "") {
            return 0;
        }
        // Split by '->' and count elements
        return listFloors.split("->").length;
    }

    @PostMapping("/delete/{areaId}")
    public String deleteArea(@PathVariable Integer areaId, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        try {
            int actor = Integer.valueOf(cookieHelper.getUserId(request));
            Area area = areaService.getAreaById(areaId);
            int buildingId = area.getBuildingId();
            areaService.deleteArea(areaId);
            redirectAttributes.addFlashAttribute("message", "Area has been deleted successfully");
            redirectAttributes.addFlashAttribute("messageType","success");
            return "redirect:/building/"+buildingId;
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            redirectAttributes.addFlashAttribute("message", errorMessage);
            redirectAttributes.addFlashAttribute("messageType","error");
            return "redirect:/area/"+areaId;
        }
//        notiHelper.createNotiForAllManagers(actor, "deleted area with id: " + areaId);
//        areaService.deleteArea(areaId);
//        return "redirect:/area";
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
