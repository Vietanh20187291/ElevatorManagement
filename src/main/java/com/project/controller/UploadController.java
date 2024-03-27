package com.project.controller;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Controller
public class UploadController {

    @GetMapping("/images/bar-chart/user-task")
    public void uploadBarChartUserTask(HttpServletResponse response) throws IOException {
        uploadImage(response, "templates/images/BarChart_UserTask.jpg");
    }

    @GetMapping("/images/bar-chart/version-task")
    public void uploadBarChartVersionTask(HttpServletResponse response) throws IOException {
        uploadImage(response, "templates/images/BarChart_VersionTask.jpg");
    }

    @GetMapping("/images/line-graph/user-task")
    public void uploadLineGraphUserTask(HttpServletResponse response) throws IOException {
        uploadImage(response, "templates/images/LineGraph_UserTask.jpg");
    }

    @GetMapping("/images/pie-chart/user-task")
    public void uploadPieChartUserTask(HttpServletResponse response) throws IOException {
        uploadImage(response, "templates/images/PieChart_UserTask.jpg");
    }

    private void uploadImage(HttpServletResponse response, String imagePath) throws IOException {
        // Đọc hình ảnh từ resources
        Resource resource = new ClassPathResource(imagePath);
        InputStream inputStream = resource.getInputStream();

        // Thiết lập kiểu content và kích thước của hình ảnh
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        response.setContentLengthLong(resource.contentLength());

        // Đẩy dữ liệu hình ảnh vào response
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        outputStream.close();
    }
}

