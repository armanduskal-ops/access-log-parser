import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class Main {

    // Собственный класс исключения для слишком длинных строк
    static class TooLongLineException extends RuntimeException {
        public TooLongLineException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        int correctFileCount = 0;
        do {
            System.out.print("Введите путь к файлу: ");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();
            if (!fileExists) {
                System.out.println("Указанный файл не существует");
                continue;
            }
            if (isDirectory) {
                System.out.println("Указанный путь ведёт к папке, а не к файлу");
                continue;
            }
            System.out.println("Путь указан верно");

            // Переменные для статистики
            int totalLines = 0;
            int maxLineLength = 0;
            int minLineLength = Integer.MAX_VALUE;

            try (FileReader fileReader = new FileReader(path);
                 BufferedReader reader = new BufferedReader(fileReader)) {

                String line;
                while ((line = reader.readLine()) != null) {
                    totalLines++;
                    int length = line.length();

                    // Проверка на максимальную длину строки
                    if (length > 1024) {
                        throw new TooLongLineException(
                                "Обнаружена строка длиннее 1024 символов! " +
                                        "Длина строки: " + length + " символов. " +
                                        "Строка: " + line.substring(0, 50) + "..."
                        );
                    }

                    // Обновление самой длинной строки
                    if (length > maxLineLength) {
                        maxLineLength = length;
                    }

                    // Обновление самой короткой строки
                    if (length < minLineLength) {
                        minLineLength = length;
                    }
                }

                // Обработка случая, когда файл пустой
                if (totalLines == 0) {
                    minLineLength = 0;
                }

                // Вывод статистики
                System.out.println("=== Статистика файла ===");
                System.out.println("Общее количество строк: " + totalLines);
                System.out.println("Длина самой длинной строки: " + maxLineLength);
                System.out.println("Длина самой короткой строки: " + minLineLength);
                System.out.println("========================");

                correctFileCount++;
                System.out.println("Это файл номер " + correctFileCount);

            } catch (TooLongLineException e) {
                // Обработка собственного исключения - завершаем программу
                System.err.println("ОШИБКА: " + e.getMessage());
                System.err.println("Программа завершена из-за слишком длинной строки.");
                return; // Завершаем выполнение программы
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } while (true);
    }
}