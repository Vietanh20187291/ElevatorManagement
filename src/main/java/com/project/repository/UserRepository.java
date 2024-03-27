package com.project.repository;

import com.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.password = :password AND u.active = true")
    User findByUsernameAndPassword(String username, String password);

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.active = true")
    Optional<User> getUserByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.userId = :userId AND u.active = true")
    Optional<User> findById(int userId);

    @Query("SELECT u FROM User u WHERE u.active = true")
    List<User> findAll();
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.active = false WHERE u.userId = :userId")
    int deactivateUser(@Param("userId") int userId);



}

