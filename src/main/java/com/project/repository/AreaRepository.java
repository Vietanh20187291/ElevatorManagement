package com.project.repository;

import com.project.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaRepository extends JpaRepository<Area, Integer> {

    @Query("SELECT a FROM Area a WHERE a.building.id = :buildingId")
    List<Area> findAreasByBuildingId(@Param("buildingId") int buildingId);
}
