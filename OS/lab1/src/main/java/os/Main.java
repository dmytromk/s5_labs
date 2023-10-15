package os;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please, enter the argument (x)");
        double x = scanner.nextDouble();
        System.out.println(Thread.activeCount());
        Manager manager = new Manager();
        manager.run(x);
    }
}