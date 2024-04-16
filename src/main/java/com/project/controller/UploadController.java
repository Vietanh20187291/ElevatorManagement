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


    @GetMapping("/images/dooropen")
    public void uploadBarChartUserTask1(HttpServletResponse response) throws IOException {
        uploadImage(response, "templates/image/open.png");
    }
    @GetMapping("/images/doorclose")
    public void uploadBarChartUserTask2(HttpServletResponse response) throws IOException {
        uploadImage(response, "templates/image/close.jpg");
    }
    @GetMapping("/images/dooropenold")
    public void uploadBarChartUserTask3(HttpServletResponse response) throws IOException {
        uploadImage(response, "templates/image/openold.png");
    }
    @GetMapping("/images/doorcloseold")
    public void uploadBarChartUserTask4(HttpServletResponse response) throws IOException {
        uploadImage(response, "templates/image/closeold.jpg");
    }
    @GetMapping("/images/simulation/text")
    public void uploadSimulationImage1(HttpServletResponse response) throws IOException {
        uploadImage(response, "templates/images/simulation/black_text.png");
    }
    @GetMapping("/images/simulation/blanket")
    public void uploadSimulationImage2(HttpServletResponse response) throws IOException {
        uploadImage(response, "templates/images/simulation/blanket2.jpg");
    }
    @GetMapping("/images/simulation/ceiling")
    public void uploadSimulationImage3(HttpServletResponse response) throws IOException {
        uploadImage(response, "templates/images/simulation/ceiling3.jpg");
    }
    @GetMapping("/images/simulation/cord")
    public void uploadSimulationImage4(HttpServletResponse response) throws IOException {
        uploadImage(response, "templates/images/simulation/cord.png");
    }
    @GetMapping("/images/simulation/door-left")
    public void uploadSimulationImage5(HttpServletResponse response) throws IOException {
        uploadImage(response, "templates/images/simulation/door-left.jpg");
    }
    @GetMapping("/images/simulation/door-right")
    public void uploadSimulationImage6(HttpServletResponse response) throws IOException {
        uploadImage(response, "templates/images/simulation/door-right.jpg");
    }
    @GetMapping("/images/simulation/indoor")
    public void uploadSimulationImage7(HttpServletResponse response) throws IOException {
        uploadImage(response, "templates/images/simulation/indoor2.png");
    }
    @GetMapping("/images/simulation/panel")
    public void uploadSimulationImage8(HttpServletResponse response) throws IOException {
        uploadImage(response, "templates/images/simulation/panel-tex5.png");
    }
    @GetMapping("/images/simulation/potting")
    public void uploadSimulationImage9(HttpServletResponse response) throws IOException {
        uploadImage(response, "templates/images/simulation/potting-2.png");
    }
    @GetMapping("/images/simulation/wall")
    public void uploadSimulationImage10(HttpServletResponse response) throws IOException {
        uploadImage(response, "templates/images/simulation/wall8.png");
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

