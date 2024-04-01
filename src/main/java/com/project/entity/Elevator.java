package com.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "elevator")
@Getter
@Setter
public class Elevator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "num_floors", nullable = false)
    private int numFloors;

    @Column(name = "area_id", nullable = false)
    private int areaId;

    @Column(name = "topic", nullable = false, unique = true)
    private String topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Area area;

}
