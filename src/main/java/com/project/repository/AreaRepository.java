package com.project.repository;

import com.project.entity.Area;
import com.project.entity.ProjectVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface AreaRepository extends JpaRepository<Area, Integer> {

    @Query("SELECT a FROM Area a WHERE a.id = :areaId")
    Optional<Area> findById(@Param("areaId") Integer areaId);


    @Query("SELECT a FROM Area a WHERE a.building.id = :buildingId")
    List<Area> findAreasByBuildingId(@Param("buildingId") int buildingId);

    @Transactional
    @Modifying
    @Query("UPDATE Area a SET a.name = :#{#area.name}, a.description = :#{#area.description} WHERE a.id = :#{#area.id}")
    void updateArea(@Param("area") Area area);

}
