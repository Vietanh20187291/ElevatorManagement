package com.project.service;

import com.project.entity.Floor;
import com.project.repository.FloorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FloorService {

    @Autowired
    private FloorRepository floorRepository;

    // Thêm 1 list các floor có cùng elevator_id
    @Transactional
    public void addListFloors(List<Floor> floors, int elevatorId) {
        for (Floor floor : floors) {
            floor.setElevatorId(elevatorId);
            floorRepository.save(floor);
        }
    }

    // Sửa (bao gồm transactional xóa tất cả các floor của elevator_id đó sau đó thêm 1 list các floor cho elevator đó)
    @Transactional
    public void updateFloorsForElevator(List<Floor> floors, int elevatorId) {
        floorRepository.deleteAllByElevatorId(elevatorId);
        for (Floor floor : floors) {
            floor.setElevatorId(elevatorId);
            floorRepository.save(floor);
        }
    }

    // Xóa tất cả các floor theo elevator_id
    @Transactional
    public void deleteFloorsByElevatorId(int elevatorId) {
        floorRepository.deleteAllByElevatorId(elevatorId);
    }

    // Lấy tất cả các floor theo elevator_id
    public List<Floor> getAllFloorsByElevatorId(int elevatorId) {
        return floorRepository.findAllByElevatorId(elevatorId);
    }
}
