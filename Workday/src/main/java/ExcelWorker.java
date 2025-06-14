import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.*;

public class ExcelWorker {


    public static List<Employee> readEmployeesFromExcel(String filePath) throws IOException {
        List<Employee> employees = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(new File(filePath));
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Пропускаем заголовок

            int id = (int) row.getCell(0).getNumericCellValue();
            String name = row.getCell(1).getStringCellValue();

            List<Integer> tasks = new ArrayList<>();
            for (int i = 2; i < row.getLastCellNum(); i++) {
                tasks.add((int) row.getCell(i).getNumericCellValue());
            }

            employees.add(new Employee(id, name, tasks));
        }

        workbook.close();
        return employees;
    }


    public static void writeResultsToExcel(List<Employee> employees, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Статистика");

        // Заголовок
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Имя");
        headerRow.createCell(2).setCellValue("Время работы");
        headerRow.createCell(3).setCellValue("Простой");
        headerRow.createCell(4).setCellValue("Задач выполнено");
        headerRow.createCell(5).setCellValue("Эффективность (%)");

        // Данные
        int rowNum = 1;
        for (Employee employee : employees) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(employee.getId());
            row.createCell(1).setCellValue(employee.getName());
            row.createCell(2).setCellValue(employee.getTotalWorkTime());
            row.createCell(3).setCellValue(employee.getIdleTime());
            row.createCell(4).setCellValue(employee.getTasksCompleted());

            double efficiency = (double) employee.getTotalWorkTime() /
                    (8 * Math.max(1, employee.getTasksCompleted() + (employee.getTasks().isEmpty() ? 0 : 1)));

            row.createCell(5).setCellValue(efficiency * 100);
        }

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }

        workbook.close();
    }
}