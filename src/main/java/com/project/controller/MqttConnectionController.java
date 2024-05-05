package com.project.controller;

import com.project.entity.MqttConnection;
import com.project.helper.CookieHelper;
import com.project.service.MqttConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class MqttConnectionController {

    @Autowired
    private MqttConnectionService mqttConnectionService;
    @Autowired
    private CookieHelper cookieHelper;

    @GetMapping("/mqtt-connection")
    public String showMqttConnection(Model model, HttpServletRequest request) {
        cookieHelper.addCookieAttributes(request, model);
        // Lấy thông tin cấu hình mqttconnection
        try {
            MqttConnection mqttConnection = mqttConnectionService.getMqttConnection();
            model.addAttribute("mqttConnection", mqttConnection);
            return "mqttconnection/detail";
        } catch (IllegalStateException e) {
            // Xử lý trường hợp không tìm thấy cấu hình mqttconnection
            return "mqtt_connection/not_found";
        }
    }


    @PostMapping("/mqtt-connection/update")
    public String updateMqttConnection(MqttConnection updatedMqttConnection, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try {
            // Lấy thông tin cấu hình mqttconnection cần cập nhật
            MqttConnection mqttConnection = mqttConnectionService.getMqttConnection();

            // Cập nhật thông tin cấu hình mqttconnection
            mqttConnection.setHost(updatedMqttConnection.getHost());
            mqttConnection.setPort(updatedMqttConnection.getPort());
            mqttConnection.setUsername(updatedMqttConnection.getUsername());
            mqttConnection.setPassword(updatedMqttConnection.getPassword());
            mqttConnection.setPath(updatedMqttConnection.getPath());

            // Cập nhật thông tin cấu hình mqttconnection
            mqttConnectionService.saveOrUpdateMqttConnection(mqttConnection);
            redirectAttributes.addFlashAttribute("message", "Configuration updated successfully");
            redirectAttributes.addFlashAttribute("messageType","success");

            return "redirect:/mqtt-connection";
        } catch (IllegalStateException e) {
            // Xử lý trường hợp không tìm thấy cấu hình mqttconnection
            return "mqtt_connection/not_found";
        }
    }

}
