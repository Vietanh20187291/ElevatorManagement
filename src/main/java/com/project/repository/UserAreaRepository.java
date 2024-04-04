package com.project.repository;

import com.project.entity.UserArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserAreaRepository extends JpaRepository<UserArea, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM UserArea ua WHERE ua.user.userId = :userId")
    void deleteByUserId(int userId);

    @Query("SELECT ua FROM UserArea ua WHERE ua.area.id = :areaId")
    List<UserArea> getUserAreaByAreaId(@Param("areaId") int areaId);

    @Query("SELECT ua FROM UserArea ua INNER JOIN ua.user u WHERE u.building.id = :buildingId AND u.active = true AND u.role = 'USER'")
    List<UserArea> getUserAreaByBuildingId(@Param("buildingId") int buildingId);

    @Query("SELECT CASE WHEN COUNT(ua) > 0 THEN true ELSE false END FROM UserArea ua WHERE ua.user.userId = :userId AND ua.area.id = :areaId")
    boolean checkDuplicates(@Param("userId") int userId, @Param("areaId") int areaId);

}
