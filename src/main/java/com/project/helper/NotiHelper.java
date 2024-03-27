package com.project.helper;

import com.project.entity.Noti;
import com.project.entity.User;
import com.project.repository.NotiRepository;
import com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class NotiHelper {
    @Autowired
    private UserService userService;
    @Autowired
    private NotiRepository notiRepository;
    public void createNoti(int actorId, String content, int recipientId) {
        Noti noti = new Noti();
        noti.setRecipient(userService.getUserById(recipientId));
        noti.setContent(userService.getUserById(actorId).getUsername()+" "+content);
        // Tạo thời gian hiện tại
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime = new Date();
        String formattedTime = dateFormat.format(currentTime);
        try {
            currentTime = dateFormat.parse(formattedTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        noti.setTimestamp(currentTime);
        noti.setUnread(true);
        notiRepository.save(noti);


    }
    public void createNotiForAllManagers(int actorId, String content) {
        createNoti(actorId,content,2);
    }
}

