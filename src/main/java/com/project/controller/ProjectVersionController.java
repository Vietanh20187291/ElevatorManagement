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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/version")
public class ProjectVersionController {
    @Autowired
    private ProjectVersionService projectVersionService;
    @Autowired
    private FeatureService featureService;
    @Autowired
    private UserProjectVersionService userProjectVersionService;
    @Autowired
    private UserService userService;
    @Autowired
    private CookieHelper cookieHelper;
    @Autowired
    private NotiHelper notiHelper;
    @Autowired
    private JwtTokenService jwtTokenService;


    @GetMapping("/{projectVersionId}")
    public String getProjectVersionById(@PathVariable Integer projectVersionId, Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        ProjectVersion projectVersion = projectVersionService.getProjectVersionById(projectVersionId);
        List<FeatureDTO> features = projectVersionService.findByProjectVersionId(projectVersionId);
        List<UserDTO> attendees = userProjectVersionService.findUsersByProjectVersionId(projectVersionId);
        List<UserDTO> users = userService.getAllUsers();

        List<UserDTO> remainingUsers = new ArrayList<>();
        for (UserDTO user : users) {
            boolean isAttendee = false;
            for (UserDTO attendee : attendees) {
                if (user.getUserId() == attendee.getUserId() || user.getRole() == UserRole.MANAGER) {
                    isAttendee = true;
                    break;
                }
            }
            if (!isAttendee) {
                remainingUsers.add(user);
            }
        }

        model.addAttribute("attendees", attendees);
        model.addAttribute("users", remainingUsers);
        model.addAttribute("projectVersion", projectVersion);
        model.addAttribute("features", features);
        return "version/projectVersion";
    }

    @GetMapping("/{projectVersionId}/edit")
    public String showEditProjectVersionForm(@PathVariable Integer projectVersionId, Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        ProjectVersion projectVersion = projectVersionService.getProjectVersionById(projectVersionId);
        model.addAttribute("projectVersion", projectVersion);
        return "version/edit";
    }

    @PostMapping("/{projectVersionId}/edit")
    public String updateProjectVersion(@PathVariable Integer projectVersionId,
                                       @ModelAttribute("projectVersion") ProjectVersion projectVersion,
                                       RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {

        int actor = Integer.valueOf(cookieHelper.getUserId(request));
        notiHelper.createNotiForAllManagers(actor,"updated version "+projectVersion.getVersion() +" of Project "+projectVersion.getProject().getProjectName());

        String token = jwtTokenService.getTokenFromRequest(request);
        User user = jwtTokenService.getUserFromToken(token);
        String redirectLink ="";
        if (user.getRole().equals(UserRole.MANAGER)){
            redirectLink = "redirect:/version/" + projectVersionId;
            System.out.println(redirectLink);
        }
        if (user.getRole().equals(UserRole.USER)){
            redirectLink = "redirect:/version/user/" +user.getUserId();
            System.out.println(redirectLink);
        }
        try {
            projectVersion.setProjectVersionId(projectVersionId);
            projectVersion.setEnable(true);
            projectVersionService.updateProjectVersion(projectVersion);
            redirectAttributes.addFlashAttribute("message", "Updated Successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
            return redirectLink ;
        }
        return redirectLink;
    }


    @PostMapping("/{projectVersionId}/disable")
    public String disableProjectVersion(@PathVariable Integer projectVersionId, RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
        int actor = Integer.valueOf(cookieHelper.getUserId(request));
        ProjectVersion pv = projectVersionService.getProjectVersionById(projectVersionId);
        notiHelper.createNotiForAllManagers(actor,"deleted version "+pv.getVersion() +" of Project "+pv.getProject());

        String token = jwtTokenService.getTokenFromRequest(request);
        User user = jwtTokenService.getUserFromToken(token);
        String redirectLink ="";
        if (user.getRole().equals(UserRole.MANAGER)){
            redirectLink = "redirect:/project";
        }
        else if (user.getRole().equals(UserRole.USER)){
            redirectLink = "redirect:/version/user/" +user.getUserId();
        } else{
            redirectLink = "redirect:/project";
        }
        try {
            projectVersionService.disableProjectVersion(projectVersionId);
            redirectAttributes.addFlashAttribute("message", "Disabled Successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return redirectLink;
    }

    @GetMapping("/{projectVersionId}/add-feature")
    public String showAddFeatureForm(@PathVariable Integer projectVersionId, Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        Feature feature = new Feature();
        feature.setProjectVersion(projectVersionService.getProjectVersionById(projectVersionId));
        model.addAttribute("feature", feature);
        return "feature/add";
    }

    @PostMapping("/{projectVersionId}/add-feature")
    public String addFeature(@PathVariable Integer projectVersionId,
                             @ModelAttribute("feature") Feature feature,
                             RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {


        String token = jwtTokenService.getTokenFromRequest(request);
        User user = jwtTokenService.getUserFromToken(token);
        String redirectLink ="";
        if (user.getRole().equals(UserRole.MANAGER)){
            redirectLink = "redirect:/version/" + projectVersionId;
        }
        else if (user.getRole().equals(UserRole.USER)){
            redirectLink = "redirect:/version/user/" +user.getUserId();
        }else {

            redirectLink = "redirect:/version/" + projectVersionId;
        }

        int actor = Integer.valueOf(cookieHelper.getUserId(request));
        ProjectVersion pv = projectVersionService.getProjectVersionById(projectVersionId);
        notiHelper.createNotiForAllManagers(actor,"added a new feature("+feature.getName()+") to version "+pv.getProjectVersionId() +" of Project "+pv.getProject().getProjectName());

        try {
            feature.setProjectVersion(projectVersionService.getProjectVersionById(projectVersionId));
            feature.setEnable(true);
            System.out.println(feature.toString());
            featureService.addFeature(feature);
            redirectAttributes.addFlashAttribute("message", "Added Successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
            return redirectLink;
        }
        System.out.println(redirectLink);
        return redirectLink;
    }

    @PostMapping("/{projectVersionId}/add-attendee")
    public String addAttendee(@PathVariable Integer projectVersionId,
                              @RequestParam Integer userId,
                              RedirectAttributes redirectAttributes,  HttpServletRequest request) {
        int actor = Integer.valueOf(cookieHelper.getUserId(request));
        ProjectVersion pv = projectVersionService.getProjectVersionById(projectVersionId);
        notiHelper.createNoti(actor,"added you as a new attendee of version "+pv.getVersion() +" of Project "+pv.getProject().getProjectName(),userId);
        try {
            User user = userService.getUserById(userId);
            ProjectVersion projectVersion = projectVersionService.getProjectVersionById(projectVersionId);
            UserProjectVersion userProjectVersion = new UserProjectVersion();
            userProjectVersion.setUser(user);
            userProjectVersion.setProjectVersion(projectVersion);
            userProjectVersion.setVersionModification(false);
            userProjectVersion.setFeatureModification(false);
            userProjectVersion.setTaskModification(false);
            userProjectVersionService.add(userProjectVersion);
            redirectAttributes.addFlashAttribute("message", "Added Attendee Successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");
            return "redirect:/version/" + projectVersionId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/version/" + projectVersionId;
    }

    @GetMapping("/{projectVersionId}/attendees")
    public String showEditAttendeesForm(@PathVariable Integer projectVersionId, Model model, HttpServletRequest request) throws Exception {
        cookieHelper.addCookieAttributes(request, model);
        List<UserProjectVersion> attendees= null;
        try {
            attendees = userProjectVersionService.findUserProjectVersionsByProjectVersionId(projectVersionId);
            System.out.println(attendees.toString());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        model.addAttribute("versionId",projectVersionId);
        model.addAttribute("attendees", attendees);
        return "attendee/attendee";
    }



    @GetMapping("/user/{userId}")
    public String getProjectVersionsByUserId(@PathVariable Integer userId, Model model, HttpServletRequest request) throws Exception {
        cookieHelper.addCookieAttributes(request, model);
        List<ProjectVersion> projectVersions = userProjectVersionService.findProjectVersionsByUserId(userId);
        List<ProjectVersionDTO> projectVersionDTOs = new ArrayList<>(); // Danh sách ProjectVersionDTO
        for (ProjectVersion projectVersion : projectVersions) {

            // Lấy danh sách Feature của ProjectVersion
            List<FeatureDTO> features = projectVersionService.findByProjectVersionId(projectVersion.getProjectVersionId());

            List<UserDTO> attendees = userProjectVersionService.findUsersByProjectVersionId(projectVersion.getProjectVersionId());

            UserProjectVersion upv = projectVersionService.getUPVByProjectVersionIdAndUserId(projectVersion.getProjectVersionId(),userId);

            ProjectVersionDTO projectVersionDTO = new ProjectVersionDTO(projectVersion, features, attendees, upv);

            projectVersionDTOs.add(projectVersionDTO);
        }



        model.addAttribute("projectVersionDTOs", projectVersionDTOs); // Truyền danh sách ProjectVersionDTO

        return "version/userProjectVersion";
    }

    @GetMapping("/{projectVersionId}/kaban-board")
    public String getKanbanBoard(@PathVariable Integer projectVersionId, Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        ProjectVersion projectVersion = projectVersionService.getProjectVersionById(projectVersionId);
        List<Task> tasks = projectVersionService.getTasksByProjectVersionId(projectVersionId);

        List<Task> inProgressTasks = new ArrayList<>();
        List<Task> toDoTasks = new ArrayList<>();
        List<Task> doneTasks = new ArrayList<>();

        for (Task task : tasks) {
            switch (task.getStatus()) {
                case PROCESSING:
                    inProgressTasks.add(task);
                    break;
                case PENDING:
                case POSTPONED:
                    toDoTasks.add(task);
                    break;
                case COMPLETED:
                    doneTasks.add(task);
                    break;
            }
        }

        model.addAttribute("projectVersion", projectVersion);
        model.addAttribute("inProgressTasks", inProgressTasks);
        model.addAttribute("toDoTasks", toDoTasks);
        model.addAttribute("doneTasks", doneTasks);

        return "version/kaban-board";
    }


}

