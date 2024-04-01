package com.project.repository;

import com.project.entity.UserArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserAreaRepository extends JpaRepository<UserArea, Integer> {

    @Transactional
    void deleteByUserId(int userId);

    @Query("SELECT ua FROM UserArea ua WHERE ua.area.id = :areaId")
    List<UserArea> getUserAreaByAreaId(@Param("areaId") int areaId);

    @Query("SELECT ua FROM UserArea ua INNER JOIN ua.user u WHERE u.building.id = :buildingId AND u.active = true AND u.role = 'USER'")
    List<UserArea> getUserAreaByBuildingId(@Param("buildingId") int buildingId);

}
