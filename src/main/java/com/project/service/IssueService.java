package com.project.service;

import com.project.entity.Issue;
import com.project.entity.Task;
import com.project.repository.IssueRepository;
import com.project.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class IssueService {
    private final IssueRepository issueRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public IssueService(IssueRepository issueRepository, TaskRepository taskRepository) {
        this.issueRepository = issueRepository;
        this.taskRepository = taskRepository;
    }


    public void deleteIssue(Integer issueId) {
        issueRepository.deleteById(issueId);
    }

    public void updateIssue(Issue issue) {
        issueRepository.save(issue);
    }

    public void addIssue(Issue issue) {
        issueRepository.save(issue);
    }



    public Issue getIssueById(Integer issueId) {
        return issueRepository.findById(issueId).orElse(null);
    }


}
