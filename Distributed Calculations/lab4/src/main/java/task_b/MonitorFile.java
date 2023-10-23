package task_b;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;

public class MonitorFile implements Runnable {
    private final ReadWriteLock readWriteLock;
    private final Garden garden;
    private final String outputPath;


    public MonitorFile(ReadWriteLock readWriteLock, Garden garden, String outputPath) {
        this.readWriteLock = readWriteLock;
        this.garden = garden;
        this.outputPath = outputPath;
    }

    @Override
    public void run() {
        while (true) {
            readWriteLock.readLock().lock();
            try {
                try (FileWriter fileWriter = new FileWriter(outputPath, true)) {
                    fileWriter.write("Current Garden:\n");
                    fileWriter.write(garden.toString() + "\n\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } finally {
                readWriteLock.readLock().unlock();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
