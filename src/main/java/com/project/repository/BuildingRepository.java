package com.project.repository;

import com.project.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Integer> {

    Optional<Building> findBuildingByName(String name);

    @Query("SELECT b FROM Building b JOIN Area a ON b.id = a.buildingId WHERE a.id = :areaId")
    Building getBuildingByAreaId(@Param("areaId") Integer areaId);
}
