package com.project.service;

import com.project.entity.MqttConnection;
import com.project.repository.MqttConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class MqttConnectionService {

    private final MqttConnectionRepository mqttConnectionRepository;

    @Autowired
    public MqttConnectionService(MqttConnectionRepository mqttConnectionRepository) {
        this.mqttConnectionRepository = mqttConnectionRepository;
    }

    public MqttConnection saveOrUpdateMqttConnection(MqttConnection mqttConnection) {
        return mqttConnectionRepository.save(mqttConnection);
    }

    public MqttConnection getMqttConnection() {
        MqttConnection connection = mqttConnectionRepository.findById(1).orElse(null);
        if (connection == null) {
            // Hoặc bạn có thể xử lý theo cách khác ở đây, ví dụ ném một exception.
            throw new IllegalStateException("MqttConnection not found!");
        }
        return connection;
    }

}
