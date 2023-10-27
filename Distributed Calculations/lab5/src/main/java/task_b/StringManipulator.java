package task_b;

import java.util.Random;

public class StringManipulator {
    private final static String characters = "ABCD";
    private final static char[] correctLetters = new char[]{'A', 'B'};

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }

        return sb.toString();
    }

    public static int countCorrectLetters(String input) {
        int result = 0;

        for (char correctLetter : correctLetters) {
            result += countLetter(input, correctLetter);
        }

        return result;
    }

    private static int countLetter(String input, char letter) {
        int count = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == letter) {
                count++;
            }
        }
        return count;
    }

    public static String randomCharTransform(String input) {
        if (input.isEmpty()) {
            return input; // Return the input string if it's empty
        }

        Random random = new Random();
        int randomIndex = random.nextInt(input.length()); // Generate a random index

        char[] result = input.toCharArray(); // Convert the input to a character array

        char originalChar = result[randomIndex];
        char transformedChar;

        switch (originalChar) {
            case 'A':
                transformedChar = 'B';
                break;
            case 'B':
                transformedChar = 'D';
                break;
            case 'C':
                transformedChar = 'A';
                break;
            case 'D':
                transformedChar = 'B';
                break;
            default:
                transformedChar = originalChar; // In case of unexpected character
                break;
        }

        result[randomIndex] = transformedChar; // Replace the character at the random index

        return new String(result); // Convert the character array back to a string
    }
}

