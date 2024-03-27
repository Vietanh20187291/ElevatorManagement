package com.project.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class FeatureDTO {
    private Feature feature;
    private List<Task> task;

    public FeatureDTO(Feature feature, List<Task> task) {
    this.feature = feature;
    this.task = task;
    }
}
