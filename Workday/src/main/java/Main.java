import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        String inputPath = "employees.xlsx";
        String outputPath = "results.xlsx";

        try {
            List<Employee> employees = ExcelWorker.readEmployeesFromExcel(inputPath);
            System.out.println("Сотрудники загружены. Начинаем рабочий день...");

            ExecutorService executor = Executors.newFixedThreadPool(4);

            for (Employee employee : employees) {
                executor.submit(employee);
            }

            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            ExcelWorker.writeResultsToExcel(employees, outputPath);
            System.out.println("Результаты сохранены в " + outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}