package com.project.service;

import com.project.entity.Elevator;
import com.project.repository.ElevatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ElevatorService {
    private ElevatorRepository elevatorRepository;

    @Autowired
    public ElevatorService(ElevatorRepository elevatorRepository) {
        this.elevatorRepository = elevatorRepository;
    }

    @Transactional
    public void deleteElevator(int elevatorId) {
        elevatorRepository.deleteById(elevatorId);
    }

    public void updateElevator(Elevator elevator) {
        elevatorRepository.updateElevator(elevator);
    }

    public int addElevator(Elevator elevator) {
        elevatorRepository.save(elevator);
        int elevatorId = elevator.getId();
        return elevatorId;
    }

    public List<Elevator> getElevatorsByAreaId(int areaId) {
        return elevatorRepository.findElevatorsByAreaId(areaId);
    }

    public Elevator getElevatorById(int elevatorId) {
        return elevatorRepository.findById(elevatorId).orElse(null);
    }

}
