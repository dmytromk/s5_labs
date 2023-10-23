package task_a;

import java.io.*;
import java.util.Map;

public class SharedFile {
    private final String filePath;

    public SharedFile(String filePath) {
        this.filePath = filePath;
    }

    public void addRecord(String name, String phone) {
        File inputFile = new File(filePath);

        try (FileWriter writer = new FileWriter(inputFile, true)) {
            writer.write(name + " - " + phone + "\n");
            System.out.println("Added record: " + name + " - " + phone);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteRecord(String name) {
        File inputFile = new File(filePath);
        File tempFile = new File("temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             FileWriter writer = new FileWriter(tempFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(name + " - ")) {
                    writer.write(line + "\n");
                } else {
                    System.out.println("Deleted record: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inputFile.delete())
            throw new RuntimeException();
        if (!tempFile.renameTo(inputFile))
            throw new RuntimeException();
    }

    public String findPhoneByName(String name) {
        File inputFile = new File(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(name)) {
                    return line.substring(name.length() + 3);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String findNameByPhone(String phone) {
        File inputFile = new File(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.endsWith(phone)) {
                    return line.substring(0, line.length() - phone.length() - 3);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}