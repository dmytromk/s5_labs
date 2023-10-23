package task_b;

import java.util.concurrent.locks.ReadWriteLock;

public class MonitorConsole implements Runnable {
    private final ReadWriteLock readWriteLock;
    private final Garden garden;


    public MonitorConsole(ReadWriteLock readWriteLock, Garden garden) {
        this.readWriteLock = readWriteLock;
        this.garden = garden;
    }

    @Override
    public void run() {
        while (true) {
            readWriteLock.readLock().lock();
            try {
                System.out.println("Current Garden:\n");
                System.out.println(garden.toString());
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
