package com.project.repository;

import com.project.entity.Elevator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElevatorRepository extends JpaRepository<Elevator, Integer> {

//
//    List<Elevator> findByCode(String code);
//
//    List<Elevator> findByNumFloors(Integer numFloors);
//
//    List<Elevator> findByCodeAndNumFloors(String code, Integer numFloors);

    List<Elevator> findElevatorsByAreaId(Integer areaId);
}
