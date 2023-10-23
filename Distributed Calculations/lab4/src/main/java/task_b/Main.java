package task_b;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
    public static void main(String[] args) {
        Garden garden = new Garden(4, 4);
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        new Thread(new Gardener(lock, garden)).start();
        new Thread(new Nature(lock, garden)).start();
        new Thread(new MonitorConsole(lock, garden)).start();
        new Thread(new MonitorFile(lock, garden, "output.txt")).start();
    }
}
