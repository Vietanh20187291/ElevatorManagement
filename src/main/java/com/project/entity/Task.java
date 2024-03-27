package com.project.entity;


import com.project.entity.enums.Priority;
import com.project.entity.enums.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "task")
@Getter
@Setter
@ToString
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private int taskId;

    @Column(name = "task_name", nullable = false)
    private String taskName;

    @Column(name = "task_description")
    private String taskDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id", referencedColumnName = "feature_id")
    private Feature feature;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to", referencedColumnName = "user_id")
    private User assignedTo;

//    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<CheckIn> checkIns;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Issue> issues;
    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Column(name = "estimated_end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date estimatedEndDate;

    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @Column(name = "progress")
    private int progress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    @Column(name = "enable")
    private boolean enable;

}
