package com.project.service;

import com.itextpdf.text.*;
import com.project.entity.EmployeeAnalysis;
import com.project.entity.User;
import com.project.repository.TaskRepository;
import com.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;

@Service

public class EmployeeAnalysisService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;
    public EmployeeAnalysis getEmployeeAnalysis(int employeeId) {
        User user = userRepository.findById(employeeId).orElse(null);
        String employeeName = user.getUsername();

        int assignedTasks = taskRepository.countByAssignedTo(employeeId);
        int completedTasks = taskRepository.countCompletedTasks(employeeId);
        LocalDate currentDate = LocalDate.now();
        Date convertedDate = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Double averageDaysOverdue = taskRepository.calculateAverageDaysOverdue(employeeId, convertedDate);
        double avgDaysOverdue = averageDaysOverdue != null ? averageDaysOverdue.doubleValue() : 0.0;
        int overdueTasks = taskRepository.countOverdueTasks(employeeId, convertedDate);
        return new EmployeeAnalysis(assignedTasks, completedTasks, overdueTasks, avgDaysOverdue,employeeName);
    }
    public int countTasksByEmployeeId(int employeeId, int pvId){
        return taskRepository.countByAssignedToAndProjectVersionId(employeeId,pvId);
    }

    public int getTotalTaskByVersionId(int versionId){
        return taskRepository.getTotalTasksInVersion(versionId);
    }


}
