package com.project.service;

import com.project.entity.Noti;
import com.project.repository.NotiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotiService {
    @Autowired
    private NotiRepository notiRepository;


    public List<Noti> getAllNotiByRecipientId(int recipientId) {
        List<Noti> notiList = notiRepository.findAllByRecipientId(recipientId);
        notiRepository.markAllAsReadByRecipientId(recipientId);
        return notiList;
    }
}

