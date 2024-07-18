package com.project.repository;

import com.project.entity.Elevator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ElevatorRepository extends JpaRepository<Elevator, Integer> {

//
//    List<Elevator> findByCode(String code);
//
//    List<Elevator> findByNumFloors(Integer numFloors);
//
//    List<Elevator> findByCodeAndNumFloors(String code, Integer numFloors);

    @Transactional
    @Modifying
    @Query("UPDATE Elevator e SET e.name = :#{#elevator.name}, e.numFloors = :#{#elevator.numFloors}, " +
            "e.listFloors = :#{#elevator.listFloors}, e.areaId = :#{#elevator.areaId}, e.topic = :#{#elevator.topic} " +
            "WHERE e.id = :#{#elevator.id}")
    void updateElevator(Elevator elevator);

    List<Elevator> findElevatorsByAreaId(Integer areaId);
}
