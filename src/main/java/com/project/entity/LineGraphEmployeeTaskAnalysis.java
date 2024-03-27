package com.project.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.YearMonth;
import java.util.Map;
@Getter
@Setter
@ToString
public class LineGraphEmployeeTaskAnalysis {
    private int userId;
    private String username;
    private Map<YearMonth, Integer> completeTask;

    public LineGraphEmployeeTaskAnalysis() {
    }

    public LineGraphEmployeeTaskAnalysis(int userId, String username, Map<YearMonth, Integer> completeTask) {
        this.userId = userId;
        this.username = username;
        this.completeTask = completeTask;
    }
}
