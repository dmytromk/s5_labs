package os;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Manager manager = new Manager();

        while (true) {
            System.out.println("\nPlease, enter the argument (x), or enter 'q' to quit:");
            String input = scanner.nextLine();

            if ("q".equalsIgnoreCase(input)) {
                // Exit the program if the user enters 'q'
                break;
            }

            try {
                double x = Double.parseDouble(input);
                manager.run(x);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

    }
}