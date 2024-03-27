package com.project.controller;

import com.project.entity.*;
import com.project.helper.CookieHelper;
import com.project.helper.NotiHelper;
import com.project.service.ProjectService;
import com.project.service.ProjectVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectVersionService projectVersionService;
    @Autowired
    private CookieHelper cookieHelper;
    @Autowired
    private NotiHelper notiHelper;


    @GetMapping
    public String getAllProjects(Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        try {
            List<Project> projects = projectService.getAllProjects();
            List<ProjectDTO> projectDTOs = new ArrayList<>();
            for (Project project : projects) {
                List<ProjectVersion> projectVersions = projectVersionService.getProjectVersionsByProjectId(project.getProjectId());
                ProjectDTO projectDTO = new ProjectDTO(project, projectVersions);
                projectDTO.toString();
                projectDTOs.add(projectDTO);
            }
            model.addAttribute("projectDTOs", projectDTOs);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return "project/projects";
    }


    @GetMapping("/{projectId}")
    public String getProjectById(@PathVariable Integer projectId, Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        Project project = projectService.getProjectById(projectId);
        List<ProjectVersion> projectVersions = projectVersionService.getProjectVersionsByProjectId(project.getProjectId());
        ProjectDTO projectDTO = new ProjectDTO(project, projectVersions);
        model.addAttribute("projectDTO", projectDTO);
        return "project/project";
    }

    @GetMapping("/add")
    public String showAddProjectForm(Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        Project project = new Project();
        model.addAttribute("project", project);
        return "project/add";
    }

    @PostMapping("/add")
    public String addProject(@ModelAttribute("project") Project project, RedirectAttributes redirectAttributes,HttpServletRequest request, Model model) {
        cookieHelper.addCookieAttributes(request, model);

        int actor = Integer.valueOf(cookieHelper.getUserId(request));
        notiHelper.createNotiForAllManagers(actor,"added a new Project: "+project.getProjectName());


        try {
            projectService.createProject(project);
            redirectAttributes.addFlashAttribute("message", "Added Successfully");
            redirectAttributes.addFlashAttribute("messageType","success");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType","error");
        }
        return "redirect:/project";
    }

    @GetMapping("/{projectId}/edit")
    public String showEditProjectForm(@PathVariable Integer projectId, Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        Project project = projectService.getProjectById(projectId);
        List<ProjectVersion> projectVersions = projectVersionService.getProjectVersionsByProjectId(project.getProjectId());
        ProjectDTO projectDTO = new ProjectDTO(project, projectVersions);
        model.addAttribute("projectDTO", projectDTO);
        return "project/edit";
    }

    @PostMapping("/{projectId}/edit")
    public String updateProject(@PathVariable Integer projectId,@ModelAttribute("project") Project project, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        int actor = Integer.valueOf(cookieHelper.getUserId(request));
        notiHelper.createNotiForAllManagers(actor,"updated project "+project.getProjectName());

        try {
            projectService.updateProject(project);
            redirectAttributes.addFlashAttribute("message", "Updated Successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/project/"+projectId+"/edit";
        }
        return "redirect:/project";
    }


    @PostMapping("/{projectId}/disable")
    public String disableProject(@PathVariable Integer projectId, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        int actor = Integer.valueOf(cookieHelper.getUserId(request));
        notiHelper.createNotiForAllManagers(actor,"deleted project "+projectService.getProjectById(projectId).getProjectName());


        try {
            projectService.disableProject(projectId);
            redirectAttributes.addFlashAttribute("message", "Disabled Successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/project";
    }


    @GetMapping("/{projectId}/versions")
    public String getProjectVersionsByProjectId(@PathVariable Integer projectId, Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        List<ProjectVersion> projectVersions = projectService.getProjectVersionsByProjectId(projectId);
        model.addAttribute("projectVersions", projectVersions);
        return "project/versions";
    }

    @PostMapping("/version/{projectVersionId}/disable")
    public String disableProjectVersion(@PathVariable Integer projectVersionId, RedirectAttributes redirectAttributes) {
        try {
            projectVersionService.disableProjectVersion(projectVersionId);
            redirectAttributes.addFlashAttribute("message", "Disabled Successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/project";
    }

    @GetMapping("/{projectId}/add-version")
    public String showAddProjectVersionForm(@PathVariable Integer projectId, Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        ProjectVersion projectVersion = new ProjectVersion();
        projectVersion.setProject(projectService.getProjectById(projectId));
        model.addAttribute("projectVersion", projectVersion);
        return "version/add";
    }

    @PostMapping("/{projectId}/add-version")
    public String addProjectVersion(@PathVariable Integer projectId,
                                    @ModelAttribute("projectVersion") ProjectVersion projectVersion,
                                    RedirectAttributes redirectAttributes, HttpServletRequest request) {
        int actor = Integer.valueOf(cookieHelper.getUserId(request));
        Project p = projectService.getProjectById(projectId);
        notiHelper.createNotiForAllManagers(actor,"added version "+projectVersion.getVersion()+" for Project "+p.getProjectName());

        try {
            projectVersion.setProject(projectService.getProjectById(projectId)); // Set Project cho ProjectVersion
            projectVersion.setProgress(0);
            projectVersion.setEnable(true);
            projectVersionService.addProjectVersion(projectVersion);
            redirectAttributes.addFlashAttribute("message", "Added Successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/" + projectId+"/add-version";
        }
        return "redirect:/project";
    }



}
