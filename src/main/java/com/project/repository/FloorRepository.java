package com.project.repository;

import com.project.entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface FloorRepository extends JpaRepository<Floor, Integer> {

    // Thêm 1 list các floor có cùng elevator_id
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO floor (name, floor_level, elevator_id) VALUES (:name, :floorLevel, :elevatorId)", nativeQuery = true)
    void addListFloor(@Param("name") String name, @Param("floorLevel") int floorLevel, @Param("elevatorId") int elevatorId);

    default void addListFloors(List<Floor> floors, int elevatorId) {
        for (Floor floor : floors) {
            addListFloor(floor.getName(), floor.getFloorLevel(), elevatorId);
        }
    }

    // Sửa (Bao gồm transactional xóa tất cả các floor của elevator_id đó sau đó thêm 1 list các floor cho elevator đó)
    @Modifying
    @Transactional
    @Query("DELETE FROM Floor f WHERE f.elevatorId = :elevatorId")
    void deleteAllByElevatorId(@Param("elevatorId") int elevatorId);

    default void updateFloorsForElevator(List<Floor> floors, int elevatorId) {
        deleteAllByElevatorId(elevatorId);
        addListFloors(floors, elevatorId);
    }

    // Xóa tất cả các floor theo elevator_id
    @Modifying
    @Transactional
    @Query("DELETE FROM Floor f WHERE f.elevatorId = :elevatorId")
    void deleteByElevatorId(@Param("elevatorId") int elevatorId);

    // Lấy tất cả các floor theo elevator_id
    @Query("SELECT f FROM Floor f WHERE f.elevatorId = :elevatorId")
    List<Floor> findAllByElevatorId(@Param("elevatorId") int elevatorId);
}
