package com.project.entity;

import com.project.entity.enums.Priority;
import com.project.entity.enums.Status;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class TaskDTO {
    private int taskId;
    private String taskName;
    private String taskDescription;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date estimatedEndDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private int progress;
    private Status status;
    private Priority priority;

    public TaskDTO(int taskId, String taskName, String taskDescription, Date startDate,
                   Date estimatedEndDate, Date endDate, int progress, Status status, Priority priority) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.startDate = startDate;
        this.estimatedEndDate = estimatedEndDate;
        this.endDate = endDate;
        this.progress = progress;
        this.status = status;
        this.priority = priority;
    }
}
