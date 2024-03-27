package com.project.service;

import com.project.entity.*;
import com.project.entity.enums.Status;
import com.project.repository.FeatureRepository;
import com.project.repository.IssueRepository;
import com.project.repository.ProjectVersionRepository;
import com.project.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private FeatureRepository featureRepository;
    @Autowired
    private ProjectVersionRepository projectVersionRepository;



    @Transactional
    public void deleteTask(Integer taskId) {
        List<Issue> issues = issueRepository.findIssuesByTaskId(taskId);
        for (Issue issue : issues) {
//            issueRepository.setEnableById(issue.getIssueId(), false);
            issueRepository.delete(issue);
        }

        taskRepository.setEnableById(taskId, false);
    }



    public void addTask(Task task) {

        taskRepository.save(task);
        updateProgress(task);
    }


    public Task getTaskById(Integer taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    public List<Issue> getIssuesByTaskId(Integer taskId) {
        return issueRepository.findIssuesByTaskId(taskId);
    }

    public TaskDTO getTaskDTOById(Integer taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task != null) {
            return new TaskDTO(
                    task.getTaskId(),
                    task.getTaskName(),
                    task.getTaskDescription(),
                    task.getStartDate(),
                    task.getEstimatedEndDate(),
                    task.getEndDate(),
                    task.getProgress(),
                    task.getStatus(),
                    task.getPriority()
            );
        }
        return null;
    }
    public void updateTask(TaskDTO taskDTO) {
        Task task = taskRepository.findById(taskDTO.getTaskId()).orElse(null);
        if (task != null) {
            task.setTaskName(taskDTO.getTaskName());
            task.setTaskDescription(taskDTO.getTaskDescription());
            task.setStartDate(taskDTO.getStartDate());
            task.setEstimatedEndDate(taskDTO.getEstimatedEndDate());
            task.setEndDate(taskDTO.getEndDate());
            task.setProgress(taskDTO.getProgress());
            if(taskDTO.getProgress()==100){
                task.setStatus(Status.COMPLETED);
                task.setEndDate(java.sql.Date.valueOf(LocalDate.now()));
            }else {
                task.setStatus(taskDTO.getStatus());
            }
            task.setPriority(taskDTO.getPriority());
            task.setEnable(true);
            taskRepository.save(task);
        }
        updateProgress(task);
    }
    public void save(Task task){
        taskRepository.save(task);
    }

//    public void updateAllProgresses() {
//        List<Task> allTasks = taskRepository.findAll();
//        for (Task task : allTasks) {
//            updateProgress(task);
//        }
//    }
//
    public void updateProgress(Task task) {
//        Task savedTask = taskRepository.save(task);

        // Tính toán progress mới cho Feature
        Feature feature = task.getFeature();
        List<Task> featureTasks = featureRepository.findTasksByFeatureId(feature.getId());
        int totalProgress = 0;
        for (Task featureTask : featureTasks) {
            totalProgress += featureTask.getProgress();
        }
        int averageProgress = featureTasks.isEmpty() ? 0 : totalProgress / featureTasks.size();
        feature.setProgress(averageProgress);
        if(feature.getProgress() == 100){
            feature.setStatus(Status.COMPLETED);}
        featureRepository.save(feature);

        // Tính toán progress mới cho ProjectVersion
        ProjectVersion projectVersion = feature.getProjectVersion();
        List<Feature> projectVersionFeatures = featureRepository.findByProjectVersionId(projectVersion.getProjectVersionId());
        totalProgress = 0;
        for (Feature projectVersionFeature : projectVersionFeatures) {
            totalProgress += projectVersionFeature.getProgress();
        }
        averageProgress = projectVersionFeatures.isEmpty() ? 0 : totalProgress / projectVersionFeatures.size();
        projectVersion.setProgress(averageProgress);
        if (projectVersion.getProgress() == 100){projectVersion.setStatus(Status.COMPLETED);}
        projectVersionRepository.save(projectVersion);

    }
    public List<User> findAttendeesByTaskId(Integer taskId) {
        List<User> attendees = taskRepository.findAttendeesByTaskId(taskId);
        return attendees;
    }

    public UserProjectVersion getUPVByTaskIdAndUserId(int taskId, int userId){
        return taskRepository.getUPVByTaskIdAndUserId(taskId,userId);
    }

    public List<Task> findTasksByUserId(int userId) {
        return taskRepository.findTasksByUserId(userId);
    }

    public List<Task> findTasksDueInDays(int days) {
        // Lấy ngày hiện tại
        Date currentDate = new Date();

        // Tính toán thời hạn bằng cách cộng thêm số ngày
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        Date dueDate = calendar.getTime();

        // Gọi hàm từ repository để lấy danh sách các task
        return taskRepository.findTasksByDueDate(currentDate, dueDate);
    }

}

