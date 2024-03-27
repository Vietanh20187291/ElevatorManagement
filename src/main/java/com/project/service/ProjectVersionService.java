package com.project.service;

import com.project.entity.*;
import com.project.repository.FeatureRepository;
import com.project.repository.ProjectVersionRepository;
import com.project.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectVersionService {
    @Autowired
    private ProjectVersionRepository projectVersionRepository;
    @Autowired
    private FeatureRepository featureRepository;
    @Autowired
    private TaskRepository taskRepository;

    public List<ProjectVersion> findAll() {
        return projectVersionRepository.findAll();
    }

    public void disableProjectVersion(Integer projectVersionId) {
        ProjectVersion projectVersion = projectVersionRepository.findById(projectVersionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid projectVersionId: " + projectVersionId));

        projectVersion.setEnable(false);
        projectVersionRepository.save(projectVersion);
    }
    public void updateProjectVersion(ProjectVersion projectVersion) {
        projectVersionRepository.save(projectVersion);
    }
    public void addProjectVersion(ProjectVersion projectVersion) {
        projectVersionRepository.save(projectVersion);
    }


    public List<ProjectVersion> getProjectVersionsByProjectId(Integer projectId) {
        return projectVersionRepository.getProjectVersionByProjectId(projectId);
    }

    public ProjectVersion getProjectVersionById(Integer projectVersionId) {
        return projectVersionRepository.findById(projectVersionId).orElse(null);
    }

    public List<FeatureDTO> findByProjectVersionId(Integer projectVersionId) {
        List<Feature> features = featureRepository.findByProjectVersionId(projectVersionId);
        List<FeatureDTO> featureDTOs = new ArrayList<>();

        for (Feature feature : features) {
            List<Task> tasks = taskRepository.findTasksByFeatureId(feature.getId());
            FeatureDTO featureDTO = new FeatureDTO(feature, tasks);
            featureDTOs.add(featureDTO);
        }

        return featureDTOs;
    }

    public List<Task> getTasksByProjectVersionId(Integer projectVersionId) {
        return projectVersionRepository.getTasksByProjectVersionId(projectVersionId);
    }

    public UserProjectVersion getUPVByProjectVersionIdAndUserId(int projectVersionId,int userId){
        return projectVersionRepository.getUPVByProjectVersionIdAndUserId(projectVersionId,userId);
    }

}
