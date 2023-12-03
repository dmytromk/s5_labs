package com.uni.common;

import java.util.Scanner;

public class InputManager {
    private final Scanner scanner;

    public InputManager() {
        this.scanner = new Scanner(System.in);
    }

    private String getLine() {
        String input;
        do {
            input = scanner.nextLine();
        } while (input == null || input.isEmpty());
        return input;
    }

    public String getString(String prompt) {
        System.out.println(prompt);
        return getLine();
    }

    public boolean getBoolean(String prompt) {
        while (true) {
            System.out.println(prompt);
            String input = getLine();
            if (input.equals("+")) {
                return true;
            } else if (input.equals("-")) {
                return false;
            } else {
                System.out.println("Invalid input: enter '+' for yes or '-' for no!");
            }
        }
    }

    public double getDouble(String prompt) {
        while (true) {
            System.out.println(prompt);
            try {
                String input = getLine();
                double res = Double.parseDouble(input);
                if (res < 0) {
                    throw new NumberFormatException("Value must be greater than 0!");
                }
                return res;
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public int getInt(String prompt) {
        while (true) {
            System.out.println(prompt);
            try {
                String input = getLine();
                int res = Integer.parseInt(input);
                if (res < 0) {
                    throw new NumberFormatException("Value must be greater than 0!");
                }
                return res;
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
