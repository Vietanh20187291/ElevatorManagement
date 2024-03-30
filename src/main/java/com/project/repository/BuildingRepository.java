package com.project.repository;

import com.project.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Integer> {

    Optional<Building> findBuildingByName(String name);
}
