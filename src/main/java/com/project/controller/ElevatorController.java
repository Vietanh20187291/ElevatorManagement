package com.project.controller;

import com.project.entity.Elevator;
import com.project.service.ElevatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/elevator")
public class ElevatorController {

    @Autowired
    private ElevatorService elevatorService;

    @GetMapping("/{elevatorId}")
    public String getElevatorById(@PathVariable int elevatorId, Model model) {
        Elevator elevator = elevatorService.getElevatorById(elevatorId);
        model.addAttribute("elevator", elevator);
        return "elevator/elevator";
    }


    @GetMapping("/{elevatorId}/edit")
    public String showEditElevatorForm(@PathVariable int elevatorId, Model model) {
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

    @PostMapping("/{elevatorId}/delete")
    public String deleteElevator(@PathVariable int elevatorId) {
        elevatorService.deleteElevator(elevatorId);
        return "redirect:/elevator";
    }

//    @GetMapping("/area/{areaId}")
//    public String getElevatorsByAreaId(@PathVariable int areaId, Model model) {
//        List<Elevator> elevators = elevatorService.getElevatorsByAreaId(areaId);
//        model.addAttribute("elevators", elevators);
//        return "elevator/elevators";
//    }
}
