package com.project.controller;

import com.project.entity.Elevator;
import com.project.helper.CookieHelper;
import com.project.service.ElevatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/elevator")
public class ElevatorController {

    @Autowired
    private ElevatorService elevatorService;
    @Autowired
    private CookieHelper cookieHelper;

    @GetMapping("/{elevatorId}")
    public String getElevatorById(@PathVariable int elevatorId, Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        Elevator elevator = elevatorService.getElevatorById(elevatorId);
        model.addAttribute("elevator", elevator);
        return "elevator/elevator";
    }


    @GetMapping("/{elevatorId}/edit")
    public String showEditElevatorForm(@PathVariable int elevatorId, Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        Elevator elevator = elevatorService.getElevatorById(elevatorId);
        model.addAttribute("elevator", elevator);
        return "elevator/edit";
    }

    @PostMapping("/{elevatorId}/edit")
    public String updateElevator(@PathVariable int elevatorId, @ModelAttribute("elevator") Elevator elevator) {
        elevator.setId(elevatorId);
        elevatorService.updateElevator(elevator);
        return "redirect:/elevator/" + elevatorId;
    }

    @PostMapping("/delete/{elevatorId}")
    public String deleteElevator(@PathVariable Integer elevatorId, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        try {
            int actor = Integer.valueOf(cookieHelper.getUserId(request));
            Elevator elevator = elevatorService.getElevatorById(elevatorId);
            int areaId = elevator.getArea().getId();
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


//    @GetMapping("/area/{areaId}")
//    public String getElevatorsByAreaId(@PathVariable int areaId, Model model) {
//        List<Elevator> elevators = elevatorService.getElevatorsByAreaId(areaId);
//        model.addAttribute("elevators", elevators);
//        return "elevator/elevators";
//    }
}
