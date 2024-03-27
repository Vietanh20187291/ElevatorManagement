package com.project.entity;

import com.project.entity.enums.IssueType;
import com.project.entity.enums.Priority;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "issue")
@Getter
@Setter
@ToString
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Integer issueId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @Enumerated(EnumType.STRING)
    @Column(name = "issue_type", nullable = false)
    private IssueType issueType;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    @Column(name = "summary", nullable = false)
    private String summary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter", nullable = false)
    private User reporter;

    @Column(name = "created_at")
    private Date createdAt;


}

