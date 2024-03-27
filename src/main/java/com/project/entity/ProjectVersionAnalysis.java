package com.project.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProjectVersionAnalysis {
    private int projectVersionId;

    private String version;
    private String projectName;
    private int totalTasks;
    private int processingTasks;
    private int completedTasks;
    private int pendingTasks;
    private int postponedTasks;
    private int overdueTasks;
    private double averageDaysOverdue;

    public ProjectVersionAnalysis(int projectVersionId, String version, String projectName, int totalTasks, int processingTasks, int completedTasks, int pendingTasks, int postponedTasks, int overdueTasks, double averageDaysOverdue) {
        this.projectVersionId = projectVersionId;
        this.version = version;
        this.projectName = projectName;
        this.totalTasks = totalTasks;
        this.processingTasks = processingTasks;
        this.completedTasks = completedTasks;
        this.pendingTasks = pendingTasks;
        this.postponedTasks = postponedTasks;
        this.overdueTasks = overdueTasks;
        this.averageDaysOverdue = averageDaysOverdue;
    }
}
