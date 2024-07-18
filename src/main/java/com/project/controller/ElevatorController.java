package com.project.controller;

import com.project.entity.Area;
import com.project.entity.Elevator;
import com.project.entity.Floor;
import com.project.entity.MqttConnection;
import com.project.helper.CookieHelper;
import com.project.service.AreaService;
import com.project.service.ElevatorService;
import com.project.service.FloorService;
import com.project.service.MqttConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/elevator")
public class ElevatorController {
    @Autowired
    private AreaService areaService;

    @Autowired
    private ElevatorService elevatorService;
    @Autowired
    private FloorService floorService;
    @Autowired
    private CookieHelper cookieHelper;
    @Autowired
    private MqttConnectionService mqttConnectionService;

    @GetMapping("/{elevatorId}")
    public String getElevatorById(@PathVariable int elevatorId, Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        Elevator elevator = elevatorService.getElevatorById(elevatorId);
        List<Elevator> elevatorList = Arrays.asList(elevator);
        model.addAttribute("elevatorList", elevatorList);
        MqttConnection mqttConnection = mqttConnectionService.getMqttConnection();
        model.addAttribute("mqttConnection", mqttConnection);
        return "elevator/elevator";
    }


    @GetMapping("/simulation/{elevatorId}")
    public String elevatorSimulation(@PathVariable Integer elevatorId, Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        Elevator elevator = elevatorService.getElevatorById(elevatorId);
        List<Elevator> elevators = Arrays.asList(elevator);
//        model.addAttribute("area", area);
        model.addAttribute("elevators", elevators);
        model.addAttribute("numFloors",elevators.get(0).getNumFloors());
        model.addAttribute("numElevators",elevators.size());
        MqttConnection mqttConnection = mqttConnectionService.getMqttConnection();
        model.addAttribute("mqttConnection", mqttConnection);
        return "elevator/simulation";
    }

    @PostMapping("/delete/{elevatorId}")
    public String deleteElevator(@PathVariable Integer elevatorId, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        try {
            int actor = Integer.valueOf(cookieHelper.getUserId(request));
            Elevator elevator = elevatorService.getElevatorById(elevatorId);
            int areaId = elevator.getAreaId();
            elevatorService.deleteElevator(elevatorId);
            redirectAttributes.addFlashAttribute("message", "Elevator has been deleted successfully");
            redirectAttributes.addFlashAttribute("messageType","success");
            return "redirect:/area/"+areaId;
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            redirectAttributes.addFlashAttribute("message", errorMessage);
            redirectAttributes.addFlashAttribute("messageType","error");
            return "redirect:/elevator/"+elevatorId;
        }
    }

    @GetMapping("/{elevatorId}/edit")
    public String showEditElevatorForm(@PathVariable Integer elevatorId, Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        Elevator elevator = elevatorService.getElevatorById(elevatorId);
        model.addAttribute("elevator", elevator);
        System.out.println(elevator.toString()+"edit");
        Area area = areaService.getAreaById(elevator.getAreaId());
        model.addAttribute("area", area);
        return "elevator/edit";
    }

    @PostMapping("/{elevatorId}/edit")
    public String editElevator(@PathVariable Integer elevatorId,
                               @ModelAttribute("elevator") Elevator elevator,
                               RedirectAttributes redirectAttributes) {
        try {
            int numFloorsCount = countFloors(elevator.getListFloors());
            elevator.setNumFloors(numFloorsCount);
            elevator.setId(elevatorId);
            System.out.println("update" + elevator.toString());
            elevatorService.updateElevator(elevator);
            redirectAttributes.addFlashAttribute("message", "Elevator has been updated successfully");
            redirectAttributes.addFlashAttribute("messageType","success");
            return "redirect:/area/"+elevator.getAreaId();
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            redirectAttributes.addFlashAttribute("message", errorMessage);
            redirectAttributes.addFlashAttribute("messageType","error");
            return "redirect:/elevator/"+elevatorId;
        }
    }
    private int countFloors(String listFloors) {
        if (listFloors == null || listFloors.isEmpty() || listFloors == "") {
            return 0;
        }
        // Split by '->' and count elements
        return listFloors.split("->").length;
    }

}
