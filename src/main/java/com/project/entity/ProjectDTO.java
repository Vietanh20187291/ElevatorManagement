package com.project.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectDTO {
    private Project project;
    private List<ProjectVersion> projectVersions;

    public ProjectDTO(Project project, List<ProjectVersion> projectVersions) {
        this.project = project;
        this.projectVersions = projectVersions;
    }
}
