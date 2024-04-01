package com.project.service;

import com.project.entity.Area;
import com.project.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AreaService {
    private AreaRepository areaRepository;

    @Autowired
    public AreaService(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    @Transactional
    public void deleteArea(Integer areaId) {
        areaRepository.deleteById(areaId);
    }

    public void updateArea(Area area) {
        areaRepository.updateArea(area);
    }

    public void addArea(Area area) {
        areaRepository.save(area);
    }

    public List<Area> getAreasByBuildingId(Integer buildingId) {
        return areaRepository.findAreasByBuildingId(buildingId);
    }

    public Area getAreaById(Integer areaId) {
        return areaRepository.findById(areaId).orElse(null);
    }
}
