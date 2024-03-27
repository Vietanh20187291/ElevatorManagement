package com.project.service;

import com.project.entity.LineGraphEmployeeTaskAnalysis;
import com.project.entity.Task;
import com.project.entity.User;
import com.project.entity.enums.UserRole;
import com.project.repository.TaskRepository;
import com.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;

@Service
public class LineGraphEmployeeTaskAnalysisService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;



    public List<LineGraphEmployeeTaskAnalysis> getAllLineGraphData() {
        List<LineGraphEmployeeTaskAnalysis> lineGraphDataList = new ArrayList<>();

        // Lấy tất cả các Task
        List<Task> tasks = taskRepository.findAll();

        // Lấy tất cả User
        List<User> users = userRepository.findAll();

        // Tạo dữ liệu cho mỗi User
        for (User user : users) {
            if (user.getRole().equals(UserRole.USER)) {
                LineGraphEmployeeTaskAnalysis lineGraphData = new LineGraphEmployeeTaskAnalysis();
                lineGraphData.setUserId(user.getUserId());
                lineGraphData.setUsername(user.getUsername());
                Map<YearMonth, Integer> completeTask = createEmptyCompleteTaskMap();

                for (Task task : tasks) {
                    if (task.getAssignedTo() != null && task.getAssignedTo().getUserId() == user.getUserId()) {
                        // Kiểm tra nhiệm vụ đã hoàn thành và có endDate không
                        if (task.getEndDate() != null) {
                            YearMonth yearMonth = YearMonth.from(task.getEndDate().toInstant().atZone(ZoneId.systemDefault()));
                            // Kiểm tra nếu YearMonth nằm trong khoảng thời gian cụ thể
                            if (isWithinRange(yearMonth, completeTask)) {
                                completeTask.put(yearMonth, completeTask.getOrDefault(yearMonth, 0) + 1);
                            }
                        }
                        }
                    }

                    // Sắp xếp completeTask theo thứ tự MonthYear tăng dần
                    Map<YearMonth, Integer> sortedCompleteTask = new TreeMap<>(Comparator.naturalOrder());
                    sortedCompleteTask.putAll(completeTask);

                // Cộng giá trị của tháng trước vào tháng sau
                Map<YearMonth, Integer> updatedMap = new HashMap<>();
                Integer temp = null;

                for (Map.Entry<YearMonth, Integer> entry : sortedCompleteTask.entrySet()) {
                    if (temp == null) {
                        temp = entry.getValue();
                    } else {
                        temp += entry.getValue();
                    }

                    updatedMap.put(entry.getKey(), temp);
                }



                    lineGraphData.setCompleteTask(updatedMap);
                    lineGraphDataList.add(lineGraphData);
                }
            }

            return lineGraphDataList;

        }


        private Map<YearMonth, Integer> createEmptyCompleteTaskMap () {
            Map<YearMonth, Integer> completeTask = new HashMap<>();

            // Lấy tháng hiện tại
            YearMonth currentMonth = YearMonth.now();

            // Thêm 5 key YearMonth với giá trị ban đầu là 0
            for (int i = 5; i > 0; i--) {
                completeTask.put(currentMonth.minusMonths(i), 0);
            }
            return completeTask;
        }

    private boolean isWithinRange(YearMonth yearMonth, Map<YearMonth, Integer> completeTask) {
        YearMonth startRange = completeTask.keySet().stream().findFirst().orElse(null);
        YearMonth endRange = completeTask.keySet().stream().reduce((first, second) -> second).orElse(null);
        return startRange != null && endRange != null && !yearMonth.isBefore(startRange) && !yearMonth.isAfter(endRange);
    }
}

