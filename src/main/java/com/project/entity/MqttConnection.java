package com.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "mqttconnection")
@Getter
@Setter
public class MqttConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "host", nullable = false)
    private String host;

    @Column(name = "port", nullable = false)
    private int port;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "path")
    private String path;

}
