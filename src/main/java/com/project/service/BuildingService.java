package com.project.service;

import com.project.entity.Building;
import com.project.repository.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class BuildingService {
    private BuildingRepository buildingRepository;

    @Autowired
    public BuildingService(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    @Transactional
    public void deleteBuilding(Integer buildingId) {
        buildingRepository.deleteById(buildingId);
    }

    public void updateBuilding(Building building) {
        buildingRepository.save(building);
    }

    public void addBuilding(Building building) {
        buildingRepository.save(building);
    }

    public Optional<Building> getBuildingByName(String name) {
        return buildingRepository.findBuildingByName(name);
    }

    public Building getBuildingById(Integer buildingId) {
        return buildingRepository.findById(buildingId).orElse(null);
    }
}
