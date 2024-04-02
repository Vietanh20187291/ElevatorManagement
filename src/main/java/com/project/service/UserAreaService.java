package com.project.service;

import com.project.entity.UserArea;
import com.project.repository.UserAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserAreaService {

    @Autowired
    private UserAreaRepository userAreaRepository;

    public List<UserArea> getUserAreaByAreaId(int areaId) {
        return userAreaRepository.getUserAreaByAreaId(areaId);
    }

    public List<UserArea> getUserAreaByBuildingId(int buildingId) {
        return userAreaRepository.getUserAreaByBuildingId(buildingId);
    }

    @Transactional
    public void updateUserAreas(List<UserArea> userAreas) {
        if (!userAreas.isEmpty()) {
            int userId = userAreas.get(0).getUser().getUserId();
            userAreaRepository.deleteByUserId(userId);
            userAreaRepository.saveAll(userAreas);
        }
    }
}
