package com.project.service;

import com.project.entity.EmployeeAnalysis;
import com.project.entity.UserDTO;
import com.project.entity.enums.UserRole;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.poi.xwpf.usermodel.*;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {
    @Autowired
    private UserService userService;

    public XWPFDocument createReport(List<EmployeeAnalysis> employeeAnalyses) {

        XWPFDocument document = new XWPFDocument();




        //tao doan van ban
        //slogan
        XWPFParagraph slogan = document.createParagraph();
        XWPFRun run = slogan.createRun();

        slogan.setAlignment(ParagraphAlignment.CENTER);
        String title = "CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM";

        run.setFontSize(17);
        //run.setFontFamily("Times New Roman");
        run.setBold(true);
        run.setText(title);

        run.addBreak();
        String title1 = "Độc lập - Tự Do - Hạnh phúc";

        run.setText(title1);

        run.addBreak();
        String title4 = "------------";

        run.setText(title4);

        run.addBreak();

       XWPFRun run2 = slogan.createRun();



        run2.setFontSize(20);
        String title2 = "BÁO CÁO HIỆU SUẤT NHÂN VIÊN";
        run2.setBold(true);

        run2.setText(title2);

//        run.addBreak();


//        XWPFRun run2 = slogan.createRun();
//        YearMonth currentMonth = YearMonth.now();
//
//
//        run2.setFontSize(12);
//        run2.setText("");
        //Paragraph 1
//        XWPFParagraph para1 = document.createParagraph();

//        para1.setAlignment(ParagraphAlignment.LEFT);
//        XWPFRun runPara1 = para1.createRun();
//
//        runPara1.setFontSize(14);
//        String text1 = "Căn cứ";
//
//        runPara1.setText(text1);
//
//        runPara1.addBreak();
//
//        runPara1.setBold(true);
//        ;
//        runPara1.setUnderline(UnderlinePatterns.THICK);
//
//        XWPFRun runPara2 = para1.createRun();
//
//        runPara2.setFontSize(11);
//        String text2 = "-  Báo cáo này tập trung vào việc phân tích đóng góp cá nhân và hiệu suất làm việc của các nhân viên trong dự án.";
//        String text3 = "-  Các quyết định phê duyệt dự án.";
//
//        runPara2.setText(text2);
//
//        runPara2.addBreak();
//
//        runPara2.setText(text3);
//
//        runPara2.addBreak();




        addGeneralInformation(document, employeeAnalyses);
        addEmployeeDataTable(document, employeeAnalyses);
        addEmployeePerformanceData(document, employeeAnalyses);
        addAdditionalInformation(document, employeeAnalyses);

        // Set Line Spacing to 1.5
        setLineSpacing(document, 1.35);


        //ghi file
        return document;
    }


    private void addAdditionalInformation(XWPFDocument document, List<EmployeeAnalysis> employeeAnalysisList) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setSpacingAfter(100);

        XWPFRun runHeading = paragraph.createRun();
        runHeading.setFontSize(17);
        runHeading.setBold(true);
        runHeading.setText("4. ĐÁNH GIÁ");
        runHeading.addBreak();

        // Add the information to the document
        XWPFRun runInfo = paragraph.createRun();
        runInfo.setFontSize(14);

        // Find employee with the most assigned tasks
        EmployeeAnalysis mostAssignedTasksEmployee = employeeAnalysisList.stream()
                .max(Comparator.comparingInt(EmployeeAnalysis::getAssignedTasks))
                .orElse(null);

        // Find employee with the least assigned tasks
        EmployeeAnalysis leastAssignedTasksEmployee = employeeAnalysisList.stream()
                .min(Comparator.comparingInt(EmployeeAnalysis::getAssignedTasks))
                .orElse(null);

        // Find employee with the most overdue tasks
        EmployeeAnalysis mostOverdueTasksEmployee = employeeAnalysisList.stream()
                .max(Comparator.comparingInt(EmployeeAnalysis::getOverdueTasks))
                .orElse(null);

        // Find employee with the least overdue tasks
        EmployeeAnalysis leastOverdueTasksEmployee = employeeAnalysisList.stream()
                .min(Comparator.comparingInt(EmployeeAnalysis::getOverdueTasks))
                .orElse(null);

        // Find employee with the highest average days overdue
        EmployeeAnalysis highestAvgDaysOverdueEmployee = employeeAnalysisList.stream()
                .max(Comparator.comparingDouble(EmployeeAnalysis::getAvgDaysOverdue))
                .orElse(null);

        // Find employee with the lowest average days overdue
        EmployeeAnalysis lowestAvgDaysOverdueEmployee = employeeAnalysisList.stream()
                .min(Comparator.comparingDouble(EmployeeAnalysis::getAvgDaysOverdue))
                .orElse(null);

        // Add the information to the document
        // Create a list of strings for the information
        List<String> info = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        info.add("     4.1 Về điều phối công việc:");

        info.add("       - Nhân viên được giao nhiều công việc nhất là " + mostAssignedTasksEmployee.getUserName() +
                " với " + mostAssignedTasksEmployee.getAssignedTasks() + " task.");

        info.add("       - Nhân viên được giao ít công việc nhất là " + leastAssignedTasksEmployee.getUserName() +
                " với  " + leastAssignedTasksEmployee.getAssignedTasks() + " task.");
        info.add("");
        info.add("     4.2 Về các công việc bị trễ:");
        info.add("       - Nhân viên có nhiều công việc quá hạn nhất là " + mostOverdueTasksEmployee.getUserName() +
                " với " + mostOverdueTasksEmployee.getOverdueTasks() + " task bị quá hạn. Trung bình mỗi task nhân viên này muộn " + mostOverdueTasksEmployee.getAvgDaysOverdue() + " ngày, cần được san sẻ bớt công việc và nỗ lực hơn để hoàn thành đúng tiến độ hơn.");

        info.add("       - Nhân viên có hoàn thành đúng hạn nhất là " + leastOverdueTasksEmployee.getUserName() +
                " với " + leastOverdueTasksEmployee.getOverdueTasks() + " công việc bị quá hạn, có thể phân bổ thêm task cho nhân viên này.");
        info.add("");

        info.add("     4.3 Về hiệu suất hoàn thành công việc:");
        EmployeeAnalysis bestPerfEmployee = employeeAnalysisList.stream().max(Comparator.comparingDouble(emp -> (double) emp.getCompletedTasks() / emp.getAssignedTasks() * 100)).get();
        info.add("       - Nhân viên " + bestPerfEmployee.getUserName() + " có tỷ lệ hoàn thành công việc cao nhất là " + String.format("%.2f", (double) bestPerfEmployee.getCompletedTasks() / bestPerfEmployee.getAssignedTasks() * 100) + "%, đóng góp tích cực vào tiến độ dự án.");

        EmployeeAnalysis worstPerfEmployee = employeeAnalysisList.stream().min(Comparator.comparingDouble(emp -> (double) emp.getCompletedTasks() / emp.getAssignedTasks() * 100)).get();
        info.add("       - Ngược lại nhân viên " + worstPerfEmployee.getUserName() + " có tỷ lệ hoàn thành công việc thấp nhất là " + String.format("%.2f", (double) worstPerfEmployee.getCompletedTasks() / worstPerfEmployee.getAssignedTasks() * 100) + "%, cần được hỗ trợ để cải thiện hiệu suất làm việc.");


        for (String inf : info) {
            runInfo.setText(inf);
            runInfo.addBreak();
        }
    }

    private void addEmployeePerformanceData(XWPFDocument document, List<EmployeeAnalysis> employeeAnalysisList) {

        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setSpacingAfter(300);

        XWPFRun runHeading = paragraph.createRun();
        runHeading.setFontSize(17);
        runHeading.setBold(true);
        runHeading.setText("3. NHẬN XÉT CÁ NHÂN");
        runHeading.addBreak();

        for (EmployeeAnalysis employee : employeeAnalysisList) {
            XWPFRun run = paragraph.createRun();
            run.setFontSize(14);
            run.setText("     - Nhân viên: " + employee.getUserName());
            run.addBreak();
            run.setText("       * Được giao " + employee.getAssignedTasks() + " task và đã hoàn thành " + employee.getCompletedTasks() + " task.");
//            run.addBreak();
//            run.setText("  * Hoàn thành " + employee.getCompletedTasks()+" task.");
            run.addBreak();
            run.setText("       * Có " + employee.getOverdueTasks() + " task quá hạn với số ngày trễ trung bình của các task quá hạn là " + employee.getAvgDaysOverdue() + ".");
           run.addBreak();
            double completionRate = (double) employee.getCompletedTasks() / employee.getAssignedTasks() * 100;
            run.setText("       * Đạt tỷ lệ hoàn thành công việc là " + String.format("%.2f", completionRate) + "%.");
            run.addBreak();
            run.addBreak();

        }

    }


    public static void addEmployeeDataTable(XWPFDocument document, List<EmployeeAnalysis> employees) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setSpacingAfter(300);

        XWPFRun runHeading = paragraph.createRun();
        runHeading.setFontSize(17);
        runHeading.setBold(true);
        runHeading.setText("2. KẾT QUẢ ĐÁNH GIÁ HIỆU SUẤT");

        // Tạo bảng với 5 cột
        XWPFTable table = document.createTable(1, 5);

        // Thiết lập các thuộc tính cho bảng
        CTTblBorders tblBorders = table.getCTTbl().getTblPr().addNewTblBorders();
        CTBorder border = tblBorders.addNewBottom();
        border.setVal(STBorder.SINGLE); // Viền liền
        border.setSz(BigInteger.valueOf(2)); // Độ dày viền (đơn vị 1/8 point, nên để 2 tương đương 1/4 point)
        border.setColor("000000"); // Mã màu viền (000000 là màu đen)

        border = tblBorders.addNewTop();
        border.setVal(STBorder.SINGLE);
        border.setSz(BigInteger.valueOf(2));
        border.setColor("000000");

        border = tblBorders.addNewLeft();
        border.setVal(STBorder.SINGLE);
        border.setSz(BigInteger.valueOf(2));
        border.setColor("000000");

        border = tblBorders.addNewRight();
        border.setVal(STBorder.SINGLE);
        border.setSz(BigInteger.valueOf(2));
        border.setColor("000000");

        border = tblBorders.addNewInsideH();
        border.setVal(STBorder.SINGLE);
        border.setSz(BigInteger.valueOf(2));
        border.setColor("000000");

        border = tblBorders.addNewInsideV();
        border.setVal(STBorder.SINGLE);
        border.setSz(BigInteger.valueOf(2));
        border.setColor("000000");

        // Thiết lập độ rộng cột cho bảng (đơn vị là twips, 1440 twips = 1 inch)
        int[] colWidths = {4000, 3000, 3000, 3000, 3000};
        for (int i = 0; i < colWidths.length; i++) {
            table.getCTTbl().addNewTblGrid().addNewGridCol().setW(BigInteger.valueOf(colWidths[i]));
        }

        // Tạo header row
        XWPFTableRow headerRow = table.getRow(0);
        headerRow.getCell(0).setText("Nhân viên");
        headerRow.getCell(1).setText("Task được giao");
        headerRow.getCell(2).setText("Task hoàn thành");
        headerRow.getCell(3).setText("Task quá hạn");
        headerRow.getCell(4).setText("Ngày quá hạn trung bình");

        // Custom tiêu đề các cột (in đậm, căn giữa)
        for (XWPFTableCell cell : headerRow.getTableCells()) {
            CTTc cttc = cell.getCTTc();
            CTTcPr ctPr = cttc.addNewTcPr();
            ctPr.addNewVAlign().setVal(STVerticalJc.CENTER); // Canh giữa dọc
            ctPr.addNewTcW().setW(BigInteger.valueOf(colWidths[headerRow.getTableCells().indexOf(cell)]));
            XWPFParagraph paragraph1 = cell.getParagraphArray(0);
            paragraph1.setAlignment(ParagraphAlignment.CENTER); // Canh giữa ngang
            XWPFRun run = paragraph1.createRun();
            run.setBold(true); // In đậm
            run.setFontSize(12); // Cỡ chữ
            run.setFontFamily("Arial"); // Font chữ
        }

        // Thêm dữ liệu các employee
        for (EmployeeAnalysis emp : employees) {
            XWPFTableRow row = table.createRow();
            row.getCell(0).setText(emp.getUserName());
            row.getCell(1).setText(String.valueOf(emp.getAssignedTasks()));
            row.getCell(2).setText(String.valueOf(emp.getCompletedTasks()));
            row.getCell(3).setText(String.valueOf(emp.getOverdueTasks()));
            row.getCell(4).setText(String.valueOf(emp.getAvgDaysOverdue()));

            // Custom dữ liệu các ô (căn giữa)
            for (XWPFTableCell cell : row.getTableCells()) {
                CTTc cttc = cell.getCTTc();
                CTTcPr ctPr = cttc.addNewTcPr();
                ctPr.addNewVAlign().setVal(STVerticalJc.CENTER); // Canh giữa dọc
                XWPFParagraph paragraph2 = cell.getParagraphArray(0);
                paragraph2.setAlignment(ParagraphAlignment.CENTER); // Canh giữa ngang
                XWPFRun run = paragraph2.createRun();
                run.setFontSize(12); // Cỡ chữ
                run.setFontFamily("Arial"); // Font chữ
                run.addBreak();
                run.addBreak();
            }
        }
    }

    private void addGeneralInformation(XWPFDocument document, List<EmployeeAnalysis> employees) {


        XWPFParagraph paragraph = document.createParagraph();

        XWPFRun runHeading = paragraph.createRun();
        runHeading.setFontSize(17);
        runHeading.setBold(true);
        runHeading.setText("1. THÔNG TIN CHUNG");
        runHeading.addBreak();

        XWPFRun run = paragraph.createRun();
        run.setFontSize(14);
        run.setText("     - Tổng số nhân viên: " + employees.size() + " người");
        run.addBreak();
        LocalDate currentDate = LocalDate.now();

        // Format the date to string (dd/MM/yyyy format)
        String dateString = currentDate.getDayOfMonth() + "/"
                + currentDate.getMonthValue() + "/"
                + currentDate.getYear();
        run.setText("     - Thời gian đánh giá: " + dateString);

    }

    private static void setLineSpacing(XWPFDocument document, double lineSpacing) {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                CTPPr ppr = paragraph.getCTP().getPPr();
                if (ppr == null) {
                    ppr = paragraph.getCTP().addNewPPr();
                }

                CTSpacing spacing = ppr.getSpacing() != null ? ppr.getSpacing() : ppr.addNewSpacing();
                spacing.setLineRule(STLineSpacingRule.AUTO);
                spacing.setLine(BigInteger.valueOf((long) (lineSpacing * 240))); // 240 twips per line
            }
        }
    }

}
