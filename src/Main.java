import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class Main {

    static class TooLongLineException extends RuntimeException {
        public TooLongLineException(String message) {
            super(message);
        }
    }

    // Класс для хранения счетчиков ботов
    static class BotCounters {
        int googlebotCount = 0;
        int yandexBotCount = 0;
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

            int totalLines = 0;
            BotCounters counters = new BotCounters();

            try (FileReader fileReader = new FileReader(path);
                 BufferedReader reader = new BufferedReader(fileReader)) {

                String line;
                while ((line = reader.readLine()) != null) {
                    totalLines++;
                    int length = line.length();

                    if (length > 1024) {
                        throw new TooLongLineException(
                                "Обнаружена строка длиннее 1024 символов! " +
                                        "Длина строки: " + length + " символов. " +
                                        "Строка: " + line.substring(0, 50) + "..."
                        );
                    }



                    // Анализ User-Agent для поиска ботов
                    analyzeUserAgent(line, counters);
                }



                // Вывод статистики файла
                System.out.println("=== Статистика файла ===");
                System.out.println("Общее количество строк: " + totalLines);

                // Вывод статистики по ботам
                System.out.println("=== Статистика поисковых ботов ===");
                if (totalLines > 0) {
                    double googlebotPercentage = (double) counters.googlebotCount / totalLines * 100;
                    double yandexBotPercentage = (double) counters.yandexBotCount / totalLines * 100;

                    System.out.printf("Запросов от Googlebot: %d (%.2f%%)\n",
                            counters.googlebotCount, googlebotPercentage);
                    System.out.printf("Запросы от YandexBot: %d (%.2f%%)\n",
                            counters.yandexBotCount, yandexBotPercentage);
                } else {
                    System.out.println("Файл пуст, статистика по ботам недоступна");
                }
                System.out.println("========================");


                correctFileCount++;
                System.out.println("Это файл номер " + correctFileCount);

            } catch (TooLongLineException e) {
                System.err.println("ОШИБКА: " + e.getMessage());
                System.err.println("Программа завершена из-за слишком длинной строки.");
                return;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } while (true);
    }

    // Метод для анализа User-Agent строки
    private static void analyzeUserAgent(String line, BotCounters counters) {
        try {
            // Ищем первую пару скобок в User-Agent
            int startBracket = line.indexOf('(');
            int endBracket = line.indexOf(')');

            if (startBracket != -1 && endBracket != -1 && endBracket > startBracket) {
                // Извлекаем содержимое первых скобок
                String firstBrackets = line.substring(startBracket + 1, endBracket);

                // Разделяем по точке с запятой
                String[] parts = firstBrackets.split(";");

                if (parts.length >= 2) {
                    // Берем второй фрагмент и очищаем от пробелов
                    String fragment = parts[1].trim();

                    // Отделяем часть до слэша
                    String programName = fragment;
                    int slashIndex = fragment.indexOf('/');
                    if (slashIndex != -1) {
                        programName = fragment.substring(0, slashIndex).trim();
                    }

                    // Проверяем, является ли программа поисковым ботом
                    if ("Googlebot".equals(programName)) {
                        counters.googlebotCount++;
                    } else if ("YandexBot".equals(programName)) {
                        counters.yandexBotCount++;
                    }
                }
            }
        } catch (Exception e) {
            // Игнорируем ошибки парсинга отдельных строк
        }
    }
}
