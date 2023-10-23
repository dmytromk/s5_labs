package task_b;

import java.util.concurrent.locks.ReadWriteLock;

public class Gardener implements Runnable {
    private final ReadWriteLock readWriteLock;
    private final Garden garden;


    public Gardener(ReadWriteLock readWriteLock, Garden garden) {
        this.readWriteLock = readWriteLock;
        this.garden = garden;
    }

    @Override
    public void run() {
        while (true) {
            readWriteLock.writeLock().lock();
            try {
                garden.waterWitheredPlants();
                System.out.println("Gardener watered plants.");
            } finally {
                readWriteLock.writeLock().unlock();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
