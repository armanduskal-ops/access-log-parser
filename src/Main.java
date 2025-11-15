import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите путь к лог-файлу: ");
        String path = scanner.nextLine();

        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            System.out.println("Указанный файл не существует или это не файл");
            return;
        }

        Statistics statistics = new Statistics();
        int totalLines = 0;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                totalLines++;

                try {
                    // Пропускаем пустые строки
                    if (line.trim().isEmpty()) {
                        continue;
                    }

                    LogEntry logEntry = new LogEntry(line);
                    statistics.addEntry(logEntry);

                } catch (Exception e) {
                    System.err.println("Ошибка при разборе строки " + totalLines + ": " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + path);
            return;
        }

        // Вывод статистики
        System.out.println("\n=== Статистика анализа лог-файла ===");
        System.out.println("Общее количество строк: " + totalLines);

        if (totalLines > 0) {
            System.out.println("\n--- Статистика трафика ---");
            System.out.println("Общий трафик: " + statistics.getTotalTraffic() + " bytes");
            System.out.printf("Средний трафик в час: %.2f bytes/hour\n", statistics.getTrafficRate());

            if (statistics.getMinTime() != null && statistics.getMaxTime() != null) {
                System.out.println("Период анализа: с " + statistics.getMinTime() + " по " + statistics.getMaxTime());
            }
        }


        scanner.close();
    }
}