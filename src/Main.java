import java.io.File;
import java.util.Scanner;

public class Main {
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
            correctFileCount++;
            System.out.println("Это файл номер " + correctFileCount);
            }
        while (true);
        }
    }