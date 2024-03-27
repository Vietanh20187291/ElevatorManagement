package com.project.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "project")
@Getter
@Setter
@ToString
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private int projectId;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_description")
    private String projectDescription;
    @Column(name = "enable")
    private boolean enable;

}

