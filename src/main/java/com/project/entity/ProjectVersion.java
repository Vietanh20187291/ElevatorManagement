package com.project.entity;
import com.project.entity.enums.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "project_version")
@Getter
@Setter
@ToString
public class ProjectVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_version_id")
    private Integer projectVersionId;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    @Column(name = "version")
    private String version;



    @Column(name = "version_description")
    private String versionDescription;

    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @Column(name = "estimated_end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date estimatedEndDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "progress")
    private Integer progress;
    @Column(name = "enable")
    private boolean enable;

}

