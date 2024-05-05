package com.project.repository;

import com.project.entity.MqttConnection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MqttConnectionRepository extends JpaRepository<MqttConnection, Integer> {
    // Các phương thức tùy chỉnh có thể được thêm vào đây nếu cần thiết
}

