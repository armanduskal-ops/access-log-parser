import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
      System.out.println("Введите первое число:");
      int number1 = new Scanner(System.in).nextInt();
        System.out.println("Введите второе число:");
        int number2 = new Scanner(System.in).nextInt();
        int number4 = number1+number2;
        System.out.println("сумма введенных чисел: "+number4);
        int number5 = number1-number2;
        System.out.println("разность введенных чисел: "+number5);
        int number3 = number1*number2;
        System.out.println("произведение введенных чисел: "+number3);
        double number6 = (double)number1/number2;
        System.out.println("произведение введенных чисел: "+number6);
    }
}
