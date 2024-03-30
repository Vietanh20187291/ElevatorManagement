package com.project.service;

import com.project.entity.Elevator;
import com.project.repository.ElevatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        elevatorRepository.save(elevator);
    }

    public void addElevator(Elevator elevator) {
        elevatorRepository.save(elevator);
    }

    public List<Elevator> getElevatorsByAreaId(int areaId) {
        return elevatorRepository.findElevatorsByAreaId(areaId);
    }

    public Elevator getElevatorById(int elevatorId) {
        return elevatorRepository.findById(elevatorId).orElse(null);
    }
}
