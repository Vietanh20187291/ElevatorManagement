package com.project.repository;

import com.project.entity.ProjectVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface ProjectVersionAnalysisRepository extends JpaRepository<ProjectVersion, Integer> {
    @Query("SELECT COUNT(t) FROM Task t WHERE t.enable = true AND t.feature.projectVersion.projectVersionId = :projectVersionId")
    Integer countTasksByProjectVersion(@Param("projectVersionId") int projectVersionId);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.enable = true AND t.feature.projectVersion.projectVersionId = :projectVersionId AND t.status = 'PROCESSING'")
    Integer countProcessingTasks(@Param("projectVersionId") int projectVersionId);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.enable = true AND t.feature.projectVersion.projectVersionId = :projectVersionId AND t.status = 'COMPLETED'")
    Integer countCompletedTasks(@Param("projectVersionId") int projectVersionId);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.enable = true AND t.feature.projectVersion.projectVersionId = :projectVersionId AND t.status = 'PENDING'")
    Integer countPendingTasks(@Param("projectVersionId") int projectVersionId);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.enable = true AND t.feature.projectVersion.projectVersionId = :projectVersionId AND t.status = 'POSTPONED'")
    Integer countPostponedTasks(@Param("projectVersionId") int projectVersionId);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.enable = true AND t.feature.projectVersion.projectVersionId = :projectVersionId AND ((t.status = 'COMPLETED' AND t.endDate > t.estimatedEndDate) OR (t.status = 'PROCESSING' AND t.estimatedEndDate < :currentDate))")
    Integer countOverdueTasks(@Param("projectVersionId") int projectVersionId, @Param("currentDate") Date currentDate);

    @Query("SELECT AVG(CASE WHEN t.status = 'COMPLETED' THEN DATEDIFF(t.endDate, t.estimatedEndDate) ELSE DATEDIFF(:currentDate, t.estimatedEndDate) END) FROM Task t WHERE t.enable = true AND t.feature.projectVersion.projectVersionId = :projectVersionId AND ((t.status = 'COMPLETED' AND t.endDate > t.estimatedEndDate) OR (t.status = 'PROCESSING' AND t.estimatedEndDate < :currentDate))")
    Double calculateAverageDaysOverdue(@Param("projectVersionId") int projectVersionId, @Param("currentDate") Date currentDate);

}
