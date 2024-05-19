package com.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "floor")
@Getter
@Setter
public class Floor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "floor_level")
    private int floorLevel;

    @Column(name = "elevator_id")
    private int elevatorId;
}
