package com.project.controller;

import com.project.entity.Issue;
import com.project.entity.Task;
import com.project.entity.User;
import com.project.helper.CookieHelper;
import com.project.service.IssueService;
import com.project.service.TaskService;
import com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/issue")
public class IssueController {
    @Autowired
    private IssueService issueService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    @Autowired
    private CookieHelper cookieHelper;


    @PostMapping("/{issueId}/delete")
    public String deleteIssue(@PathVariable Integer issueId, RedirectAttributes redirectAttributes) {
        Issue issue = issueService.getIssueById(issueId);
        Integer taskId = issue.getTask().getTaskId();

        try {
            issueService.deleteIssue(issueId);
            redirectAttributes.addFlashAttribute("message", "Deleted Successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        // Redirect to the appropriate page
        return "redirect:/task/"+taskId;
    }

    @GetMapping("/{issueId}/edit")
    public String showEditIssueForm(@PathVariable Integer issueId, Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        Issue issue = issueService.getIssueById(issueId);
        System.out.println("hi"+issue.getTask().getTaskId());
        model.addAttribute("issue", issue);
        return "issue/edit";
    }

    @PostMapping("/{issueId}/edit")
    public String updateIssue(@PathVariable Integer issueId,
                              @ModelAttribute("issue") Issue issue,
                              RedirectAttributes redirectAttributes, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String userId = null;
        userId = cookieHelper.getUserId(request);
        User user = userService.getUserById(Integer.parseInt(userId));
        System.out.println(issue.toString());
        try {
            issue.setIssueId(issueId);
            issue.setReporter(user);
            issueService.updateIssue(issue);
            redirectAttributes.addFlashAttribute("message", "Updated Successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/issue/" + issueId + "/edit";
        }
        return "redirect:/task/" + issue.getTask().getTaskId();
    }

    @GetMapping("/{issueId}")
    public String getIssueById(@PathVariable Integer issueId, Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        Issue issue = issueService.getIssueById(issueId);
        model.addAttribute("issue", issue);
        return "issue/issue";
    }


}

