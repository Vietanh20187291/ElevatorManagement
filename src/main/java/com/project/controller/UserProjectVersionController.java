package com.project.controller;

import com.project.entity.UserProjectVersion;
import com.project.helper.CookieHelper;
import com.project.helper.NotiHelper;
import com.project.service.ProjectVersionService;
import com.project.service.UserProjectVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/attendee")
public class UserProjectVersionController {
    @Autowired
    private UserProjectVersionService userProjectVersionService;
    @Autowired
    private NotiHelper notiHelper;
    @Autowired
    private CookieHelper cookieHelper;
    @Autowired
    private ProjectVersionService projectVersionService;
    @PostMapping("/{userProjectVersionId}/edit")
    public String editAttendee(@PathVariable Integer userProjectVersionId,
                               @ModelAttribute UserProjectVersion attendee,
                               RedirectAttributes redirectAttributes,  HttpServletRequest request) {

        UserProjectVersion upv = userProjectVersionService.findById(userProjectVersionId);
        upv.setVersionModification(attendee.isVersionModification());
        upv.setFeatureModification(attendee.isFeatureModification());
        upv.setTaskModification(attendee.isTaskModification());

        int actor = Integer.valueOf(cookieHelper.getUserId(request));
        System.out.println(upv.toString());
        notiHelper.createNoti(actor,"updated your permission in version "+upv.getProjectVersion().getVersion() +" of Project "+upv.getProjectVersion().getProject().getProjectId(), upv.getUser().getUserId());

        try {
            userProjectVersionService.editUserProjectVersion(upv);
            redirectAttributes.addFlashAttribute("message", "Updated Attendee Successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/version/"+upv.getProjectVersion().getProjectVersionId()+"/attendees";
    }
    @PostMapping("/{userProjectVersionId}/delete")
    public String deleteAttendee(@PathVariable Integer userProjectVersionId,
                                 RedirectAttributes redirectAttributes,  HttpServletRequest request) {
        UserProjectVersion upv = userProjectVersionService.findById(userProjectVersionId);
        Integer projectVersionId = upv.getProjectVersion().getProjectVersionId();

        int actor = Integer.valueOf(cookieHelper.getUserId(request));
        notiHelper.createNoti(actor,"removed you from version "+upv.getProjectVersion().getVersion() +" of Project "+upv.getProjectVersion().getProject().getProjectId(), upv.getUser().getUserId());


        try {
            userProjectVersionService.deleteUserProjectVersion(userProjectVersionId);
            redirectAttributes.addFlashAttribute("message", "Deleted Attendee Successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/version/"+projectVersionId+"/attendees";

    }


}
