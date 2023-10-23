package task_a;

public class Main {
    public static void main(String[] args) {
        // Specify the file path
        String filePath = "records.txt";

        SharedFile sharedFile = new SharedFile(filePath);
        MyReadWriteLock myReadWriteLock = new MyReadWriteLock();

        // Add records
        new Thread(new RecordWriter(sharedFile, "add", "Alice", "123-456-7890", myReadWriteLock)).start();
        new Thread(new RecordWriter(sharedFile, "add", "Bob", "987-654-3210", myReadWriteLock)).start();

        // Find phone by name
        new Thread(new PhoneFinder(sharedFile, "Alice", myReadWriteLock)).start();
        new Thread(new PhoneFinder(sharedFile, "Charlie", myReadWriteLock)).start();

        // Find name by phone
        new Thread(new NameFinder(sharedFile, "987-654-3210", myReadWriteLock)).start();
        new Thread(new NameFinder(sharedFile, "555-555-5555", myReadWriteLock)).start();

        // Delete records
        new Thread(new RecordWriter(sharedFile, "delete", "Bob", "", myReadWriteLock)).start();
    }
}

