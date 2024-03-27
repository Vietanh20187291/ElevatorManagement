package com.project.service;

import com.project.entity.Project;
import com.project.entity.ProjectVersion;
import com.project.repository.ProjectRepository;
import com.project.repository.ProjectVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectVersionRepository projectVersionRepository;

//    public ProjectService(ProjectRepository projectRepository, ProjectVersionRepository projectVersionRepository) {
//        this.projectRepository = projectRepository;
//        this.projectVersionRepository = projectVersionRepository;
//    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Integer projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid projectId: " + projectId));
    }

    public Project createProject(Project project) {
        project.setEnable(true);
        return projectRepository.save(project);
    }

    public Project updateProject(Project project) {
        return projectRepository.save(project);
    }

    @Transactional
    public void disableProject(Integer projectId) {
        List<ProjectVersion> projectVersions = projectVersionRepository.getProjectVersionByProjectId(projectId);
        projectVersions.forEach(projectVersion -> projectVersion.setEnable(false));
        projectRepository.setEnableById(projectId, false);
    }

    public List<ProjectVersion> getProjectVersionsByProjectId(Integer projectId) {
        return projectVersionRepository.getProjectVersionByProjectId(projectId);
    }
}

