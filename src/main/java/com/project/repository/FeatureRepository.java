package com.project.repository;

import com.project.entity.Feature;
import com.project.entity.Task;
import com.project.entity.User;
import com.project.entity.UserProjectVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Integer> {
    @Modifying
    @Query("UPDATE Feature f SET f.enable = :enable WHERE f.id = :featureId")
    int setEnableById(@Param("featureId") Integer featureId, @Param("enable") boolean enable);

    @Override
    @Query("SELECT f FROM Feature f WHERE f.enable = true")
    List<Feature> findAll();

    @Override
    @Query("SELECT f FROM Feature f WHERE f.enable = true AND f.id = :id")
    Optional<Feature> findById(@Param("id") Integer id);

    @Query("SELECT f FROM Feature f WHERE f.projectVersion.projectVersionId = :projectVersionId AND f.enable = true")
    List<Feature> findByProjectVersionId(@Param("projectVersionId") Integer projectVersionId);

    @Query("SELECT t FROM Task t WHERE t.feature.id = :featureId AND t.feature.enable = true")
    List<Task> findTasksByFeatureId(@Param("featureId") Integer featureId);

    @Query("SELECT DISTINCT upv.user FROM UserProjectVersion upv WHERE upv.projectVersion IN (SELECT f.projectVersion FROM Feature f WHERE f.id = :featureId)")
    List<User> findAttendeesByFeatureId(@Param("featureId") Integer featureId);

    @Query("SELECT upv FROM UserProjectVersion upv " +
            "JOIN upv.projectVersion pv " +
            "WHERE upv.user.userId = :userId " +
            "AND pv.projectVersionId = (SELECT f.projectVersion.projectVersionId FROM Feature f WHERE f.id = :featureId)")
    UserProjectVersion getUPVByFeatureIdAndUserId(@Param("featureId") int featureId, @Param("userId") int userId);

}
