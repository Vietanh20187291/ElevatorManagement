package com.project.service;

import com.project.entity.Building;
import com.project.entity.User;
import com.project.entity.UserDTO;
import com.project.entity.enums.UserRole;
import com.project.repository.UserRepository;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.KeyManagerFactory;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    TaskService taskService;

    public boolean checkLogin(User user) {
        User userFromDB = null;
        try {
            userFromDB = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (userFromDB != null && userFromDB.isActive()) {
            return true;
        } else {
            System.out.println("cannot found");
            return false;

        }

//        String host = "tkevn.ddns.net";  // hostname or IP address
//        int port = 8000;
//        String clientId = MqttAsyncClient.generateClientId();
//        String topic = "test/topic";
//        String content = "Hello";
//        int qos = 0;
//        String protocol = "ssl://";
//
//        String username = "user1";
//        String password = "minh";
//
//        String certPath = "src/main/java/com/project/service/client.crt";
//        String keyPath = "src/main/java/com/project/service/client.key";
//        String caPath = "src/main/java/com/project/service/rootCA.crt";
//
//        Logger.getLogger("org.eclipse.paho.client").setLevel(Level.FINE);
//        try {
//            MqttClient client = new MqttClient(protocol + host + ":" + port, clientId, new MemoryPersistence());
//
//            MqttConnectOptions connOpts = new MqttConnectOptions();
//            connOpts.setCleanSession(true);
//            connOpts.setUserName(username);
//            connOpts.setPassword(password.toCharArray());
//
//
//            // Load client certificate
//            CertificateFactory cf = CertificateFactory.getInstance("X.509");
//            FileInputStream clientCertFile = new FileInputStream(certPath);
//            X509Certificate clientCert = (X509Certificate) cf.generateCertificate(clientCertFile);
//            clientCertFile.close();
//
//            // Load client key
//            FileInputStream keyFile = new FileInputStream(keyPath);
//            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//            ks.load(null, null);
//            ks.setCertificateEntry("client", clientCert);
//            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//            kmf.init(ks, null);
//            keyFile.close();
//
//            // Load CA certificate
//            FileInputStream caFile = new FileInputStream(caPath);
//            X509Certificate caCert = (X509Certificate) cf.generateCertificate(caFile);
//            caFile.close();
//
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
//            caKs.load(null, null);
//            caKs.setCertificateEntry("ca", caCert);
//            tmf.init(caKs);
//
//            // Create SSL context
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
//
//            connOpts.setSocketFactory(sslContext.getSocketFactory());
//
//            System.out.println("Connecting to broker: " + host);
//            client.connect(connOpts);
//            System.out.println("Connected");
//
//            System.out.println("Publishing message: " + content);
//            MqttMessage message = new MqttMessage(content.getBytes());
//            message.setQos(qos);
//            client.publish(topic, message);
//            System.out.println("Message published");
//
//            client.disconnect();
//            System.out.println("Disconnected");
//            System.exit(0);
//        } catch (MqttException me) {
//            System.out.println("Reason: " + me.getReasonCode());
//            System.out.println("Message: " + me.getMessage());
//            System.out.println("Localized Message: " + me.getLocalizedMessage());
//            System.out.println("Cause: " + me.getCause());
//            System.out.println("Exception: " + me);
//            me.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return true;

    }

    public User getUserByUsername(String username) {
        Optional<User> user = userRepository.getUserByUsername(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public User getUserById(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void addUser(String username, String password, UserRole role, Building building) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        user.setBuilding(building);
        user.setActive(true);
        userRepository.save(user);
    }


    public void deactivateUser(int userId) {
        userRepository.deactivateUser(userId);
    }

    public User updateUserRole(int userId, UserRole newRole) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(newRole);

        return userRepository.save(user);
    }

    public User updatePassword(int userId, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(newPassword);

        return userRepository.save(user);
    }

    public List<UserDTO> getUsersByBuildingId(int buildingId) {
        List<User> users = userRepository.getUsersByBuildingId(buildingId);
        List<UserDTO> userDTOs = new ArrayList<>();

        for (User user : users) {
            if (user.getRole() != UserRole.ADMIN && user.getRole() != UserRole.MANAGER) {
                if (user.isActive()) {
                    UserDTO userDTO = new UserDTO(user.getUserId(), user.getUsername(), user.getRole());
                    userDTOs.add(userDTO);
                }
            }
        }

        return userDTOs;
    }


    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();

        for (User user : users) {
            if (user.getRole() != UserRole.ADMIN || user.getRole() != UserRole.MANAGER) {
                if (user.isActive()) {
                    UserDTO userDTO = new UserDTO(user.getUserId(), user.getUsername(), user.getRole());
                    userDTOs.add(userDTO);
                }
            }
        }

        return userDTOs;
    }


}

