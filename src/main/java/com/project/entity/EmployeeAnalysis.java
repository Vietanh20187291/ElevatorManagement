package com.project.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;


@Getter
@Setter
@ToString
public class EmployeeAnalysis {
    private int assignedTasks;
    private int completedTasks;
    private int overdueTasks;

    private double avgDaysOverdue;
    
    private String userName;


    public EmployeeAnalysis(int assignedTasks, int completedTasks, int overdueTasks, double avgDaysOverdue, String userName) {
    this.assignedTasks = assignedTasks;
    this.completedTasks = completedTasks;
    this.overdueTasks = overdueTasks;
    this.avgDaysOverdue = avgDaysOverdue;
    this.userName = userName;
    }
}
