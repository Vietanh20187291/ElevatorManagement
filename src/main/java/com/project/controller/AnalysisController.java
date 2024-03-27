package com.project.controller;

import com.project.entity.*;
import com.project.entity.enums.UserRole;
import com.project.helper.CookieHelper;
import com.project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AnalysisController {

    @Autowired
    LineGraphEmployeeTaskAnalysisService lineGraphEmployeeTaskAnalysisService;
    @Autowired
    private EmployeeAnalysisService employeeAnalysisService;
    @Autowired
    private ProjectVersionService projectVersionService;
    @Autowired
    private ProjectVersionAnalysisService projectVersionAnalysisService;
    @Autowired
    private UserService userService;
    @Autowired
    private CookieHelper cookieHelper;

    @GetMapping("/analysis")
    public String getAnalysisMainPage(Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        return "analysis/MainPage";
    }


    private List<EmployeeAnalysis> getListEmployeeAnalysis() {
        List<UserDTO> users = userService.getAllUsers();
        users.removeIf(user -> user.getRole().equals(UserRole.MANAGER));
        List<EmployeeAnalysis> employeeAnalyses = new ArrayList<>();

        for (UserDTO user : users) {
            EmployeeAnalysis employeeAnalysis = employeeAnalysisService.getEmployeeAnalysis(user.getUserId());
            System.out.println(employeeAnalysis.toString());
            employeeAnalyses.add(employeeAnalysis);
        }
        return employeeAnalyses;
    }

    @GetMapping("/analysis/bar-chart/user-task")
    public String getUserAnalysis(Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        List<UserDTO> users = userService.getAllUsers();
        users.removeIf(user -> user.getRole().equals(UserRole.MANAGER));
        List<EmployeeAnalysis> employeeAnalyses = new ArrayList<>();

        for (UserDTO user : users) {
            EmployeeAnalysis employeeAnalysis = employeeAnalysisService.getEmployeeAnalysis(user.getUserId());
            System.out.println(employeeAnalysis.toString());
            employeeAnalyses.add(employeeAnalysis);
        }

        model.addAttribute("employeeAnalyses", employeeAnalyses);

        return "analysis/BarChart_UserTask";
    }

    @GetMapping("/analysis/pie-chart/user-task/version/{versionId}")
    public String getUserAnalysisPieChart(@PathVariable Integer versionId,Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        List<UserDTO> users = userService.getAllUsers();
        users.removeIf(user -> user.getRole().equals(UserRole.MANAGER));
        List<Map<String, Object>> employeeTasks = new ArrayList<>();

        for (UserDTO user : users) {
            int employeeId = user.getUserId();
            int tasksCount = employeeAnalysisService.countTasksByEmployeeId(employeeId, versionId);
            String employeeName = user.getUsername();
            System.out.println(employeeId + ":" + tasksCount);
            Map<String, Object> employeeTaskMap = new HashMap<>();
            employeeTaskMap.put("userName", employeeName);
            employeeTaskMap.put("taskCount", tasksCount);
            employeeTasks.add(employeeTaskMap);
        }

        model.addAttribute("employeeTasks", employeeTasks);

        int totalTasks = employeeAnalysisService.getTotalTaskByVersionId(versionId);
        System.out.println(totalTasks);
        model.addAttribute("totalTasks", totalTasks);
        model.addAttribute("versionId",versionId);

        return "analysis/PieChart_UserTask";
    }

    @GetMapping("/analysis/bar-chart/version-task")
    public String getProjectVersionAnalysis(Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);

        List<ProjectVersion> projectVersions = projectVersionService.findAll();
        List<ProjectVersionAnalysis> projectVersionAnalyses = new ArrayList<>();

        for (ProjectVersion projectVersion : projectVersions) {
            ProjectVersionAnalysis projectVersionAnalysis = projectVersionAnalysisService.getProjectVersionAnalysis(projectVersion.getProjectVersionId());
            projectVersionAnalyses.add(projectVersionAnalysis);
        }

        model.addAttribute("projectVersionAnalyses", projectVersionAnalyses);

        return "analysis/BarChart_VersionTask";

    }

    @GetMapping("/analysis/line-graph/user-task")
    public String getLineGraphEmployeeTask(Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);

        // Lấy danh sách LineGraphEmployeeTaskAnalysis từ service
        List<LineGraphEmployeeTaskAnalysis> lineGraphDataList = lineGraphEmployeeTaskAnalysisService.getAllLineGraphData();

        // Thêm danh sách LineGraphEmployeeTaskAnalysis vào model
        model.addAttribute("lineGraphDataList", lineGraphDataList);

        return "analysis/LineGraph_UserTask"; // Trả về tên view mà bạn muốn hiển thị dữ liệu
    }


}

