package com.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@Entity
@Table(name = "user_area")
@Getter
@Setter
public class UserArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id")
    private Area area;

}
