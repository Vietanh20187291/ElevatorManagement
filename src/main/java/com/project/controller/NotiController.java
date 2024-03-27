package com.project.controller;

import com.project.entity.Issue;
import com.project.entity.Noti;
import com.project.helper.CookieHelper;
import com.project.service.NotiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/noti")
public class NotiController {
    @Autowired
    private NotiService notiService;
    @Autowired
    private CookieHelper cookieHelper;


//    @GetMapping("/{recipientId}")
//    public String getNotiByRecipientId(@PathVariable int recipientId, Model model) {
//
//        List listNoti = notiService.getAllNotiByRecipientId(recipientId);
//        model.addAttribute("listNoti", listNoti);
//        return "noti/noti";
//    }

    @GetMapping("/{recipientId}")
    public String getNotiByRecipientId(@PathVariable int recipientId, Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
       List<Noti> listNoti = notiService.getAllNotiByRecipientId(recipientId);


        List<Noti> readNotiList = new ArrayList<Noti>();
        List<Noti> unreadNotiList = new ArrayList<Noti>();
        for (int i = listNoti.size() -1; i > -1; i--) {
            if (!listNoti.get(i).isUnread()) {
                readNotiList.add(listNoti.get(i));
            } else {
                unreadNotiList.add(listNoti.get(i));
            }
        }
        model.addAttribute("readNotiList", readNotiList);
        model.addAttribute("unreadNotiList", unreadNotiList);

        return "noti/noti";
    }



}
