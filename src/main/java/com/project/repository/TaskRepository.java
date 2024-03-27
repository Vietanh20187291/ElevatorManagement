package com.project.repository;
import com.project.entity.Task;
import com.project.entity.User;
import com.project.entity.UserProjectVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Override
    @Query("SELECT t FROM Task t WHERE t.enable = true")
    List<Task> findAll();

    @Override
    @Query("SELECT t FROM Task t WHERE t.enable = true AND t.taskId = :id")
    Optional<Task> findById(@Param("id") Integer id);


    @Query("SELECT t FROM Task t WHERE t.feature.id = :featureId AND t.enable = true")
    List<Task> findTasksByFeatureId(@Param("featureId") int featureId);

    @Query("SELECT t FROM Task t WHERE t.assignedTo.userId = :userId AND t.enable = true")
    List<Task> findTasksByUserId(@Param("userId") int userId);

    @Modifying
    @Query("UPDATE Task t SET t.enable = :enable WHERE t.taskId = :taskId")
    int setEnableById(@Param("taskId") Integer taskId, @Param("enable") boolean enable);

    @Query("SELECT DISTINCT upv.user FROM UserProjectVersion upv WHERE upv.projectVersion IN (SELECT t.feature.projectVersion FROM Task t WHERE t.taskId = :taskId)")
    List<User> findAttendeesByTaskId(@Param("taskId") Integer taskId);

//    @Query("SELECT COUNT(t) FROM Task t WHERE t.enable = true")
//    int getTotalTask();

    @Query("SELECT COUNT(t) FROM Task t " +
            "JOIN t.feature f " +
            "JOIN f.projectVersion pv " +
            "WHERE t.enable = true " +
            "AND pv.projectVersionId = :projectVersionId")
    int getTotalTasksInVersion(@Param("projectVersionId") int projectVersionId);



    @Query("SELECT COUNT(t) FROM Task t WHERE t.enable = true AND t.assignedTo.userId = :employeeId")
    Integer countByAssignedTo(@Param("employeeId") int employeeId);

    @Query("SELECT COUNT(t) FROM Task t " +
            "JOIN t.feature f " +
            "JOIN f.projectVersion pv " +
            "WHERE t.enable = true " +
            "AND t.assignedTo.userId = :employeeId " +
            "AND pv.projectVersionId = :projectVersionId")
    Integer countByAssignedToAndProjectVersionId(
            @Param("employeeId") int employeeId,
            @Param("projectVersionId") int projectVersionId);


    @Query("SELECT COUNT(t) FROM Task t WHERE t.enable = true AND t.assignedTo.userId = :employeeId AND t.status = 'COMPLETED'")
    Integer countCompletedTasks(@Param("employeeId") int employeeId);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.enable = true AND t.assignedTo.userId = :employeeId AND ((t.status = 'COMPLETED' AND t.endDate > t.estimatedEndDate) OR (t.status = 'PROCESSING' AND t.estimatedEndDate < :currentDate))")
    Integer countOverdueTasks(@Param("employeeId") int employeeId, @Param("currentDate") Date currentDate);

    @Query("SELECT AVG(CASE WHEN t.status = 'COMPLETED' THEN DATEDIFF(t.endDate, t.estimatedEndDate) ELSE DATEDIFF(:currentDate, t.estimatedEndDate) END) FROM Task t WHERE t.enable = true AND t.assignedTo.userId = :employeeId AND ((t.status = 'COMPLETED' AND t.endDate > t.estimatedEndDate) OR (t.status = 'PROCESSING' AND t.estimatedEndDate < :currentDate))")
    Double calculateAverageDaysOverdue(@Param("employeeId") int employeeId, @Param("currentDate") Date currentDate);

    @Query("SELECT upv FROM UserProjectVersion upv " +
            "JOIN upv.projectVersion pv " +
            "WHERE upv.user.userId = :userId " +
            "AND pv.projectVersionId = (SELECT f.projectVersion.projectVersionId FROM Feature f WHERE f.id = " +
            "(SELECT t.feature.id FROM Task t WHERE t.taskId = :taskId))")
    UserProjectVersion getUPVByTaskIdAndUserId(@Param("taskId") int taskId, @Param("userId") int userId);

    @Query("SELECT t FROM Task t WHERE t.enable = true AND t.endDate = null AND t.estimatedEndDate BETWEEN :currentDate AND :dueDate")
    List<Task> findTasksByDueDate(@Param("currentDate") Date currentDate, @Param("dueDate") Date dueDate);



}

