package com.project.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "check_in")
@Getter
@Setter
@ToString
public class CheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "summary")
    private String summary;

    @Column(name = "day")
    private LocalDateTime day;

    @Column(name = "duration")
    private int duration;

}

