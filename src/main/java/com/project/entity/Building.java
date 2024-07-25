package com.project.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "building")
@Getter
@Setter
@ToString
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    private String name;


    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "note", nullable = true)
    private String note;

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL)
    private List<User> users;

}


