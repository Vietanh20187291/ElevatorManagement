package com.project.repository;

import com.project.entity.ProjectVersion;
import com.project.entity.User;
import com.project.entity.UserProjectVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProjectVersionRepository extends JpaRepository<UserProjectVersion, Integer> {

    @Query("SELECT pv FROM ProjectVersion pv INNER JOIN UserProjectVersion upv ON pv.projectVersionId = upv.projectVersion.projectVersionId WHERE upv.user.userId = :userId AND pv.enable = true")
    List<ProjectVersion> findProjectVersionsByUserId(Integer userId);

    @Query("SELECT u FROM User u INNER JOIN UserProjectVersion upv ON u.userId = upv.user.userId WHERE upv.projectVersion.projectVersionId = :projectVersionId AND u.active = true")
    List<User> findUsersByProjectVersionId(Integer projectVersionId);

    @Query("SELECT upv FROM UserProjectVersion upv WHERE upv.projectVersion.projectVersionId = :projectVersionId")
    List<UserProjectVersion> findByProjectVersionId(Integer projectVersionId);


}

