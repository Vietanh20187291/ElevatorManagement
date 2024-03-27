package com.project.controller;

import com.project.entity.EmployeeAnalysis;
import com.project.entity.UserDTO;
import com.project.entity.enums.UserRole;
import com.project.service.EmployeeAnalysisService;
import com.project.service.ReportService;
import com.project.service.UserService;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Controller
public class ReportController {
    @Autowired
    private ReportService reportService;

    @Autowired
    private UserService userService;
    @Autowired
    private EmployeeAnalysisService employeeAnalysisService;

    @GetMapping("/report")
    public ResponseEntity<ByteArrayResource> generateFile() throws IOException {
        // Create the XWPFDocument using the createReport method
        List<EmployeeAnalysis> employeeAnalyses = getListEmployeeAnalysis();
        XWPFDocument document = reportService.createReport(employeeAnalyses); // Provide the list of employees as needed

        // Convert the XWPFDocument to a downloadable format (DOCX) using ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.write(byteArrayOutputStream);

        // Convert ByteArrayOutputStream to ByteArrayResource
        ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

        // Set the response headers to make the file downloadable
        HttpHeaders headers = new HttpHeaders();
        LocalDate currentDate = LocalDate.now();

        // Format the date to string (dd/MM/yyyy format)

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=BaoCao_HieuSuatNV_"+currentDate.getDayOfMonth() + currentDate.getMonthValue() + currentDate.getYear()+".docx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        // Return the file as a ResponseEntity
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    private List<EmployeeAnalysis> getListEmployeeAnalysis(){
        List<UserDTO> users = userService.getAllUsers();
        users.removeIf(user -> user.getRole().equals(UserRole.MANAGER));
        List<EmployeeAnalysis> employeeAnalyses = new ArrayList<>();

        for (UserDTO user : users) {
            EmployeeAnalysis employeeAnalysis = employeeAnalysisService.getEmployeeAnalysis(user.getUserId());
            System.out.println(employeeAnalysis.toString());
            employeeAnalyses.add(employeeAnalysis);
        }
        return employeeAnalyses;
    }
}
