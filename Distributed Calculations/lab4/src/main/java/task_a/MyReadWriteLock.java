package task_a;

public class MyReadWriteLock {
    private int readers = 0;
    private int writers = 0;

    public synchronized void acquireReadLock() {
        while (writers != 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        readers++;
    }

    public synchronized void releaseReadLock() {
        if(readers == 0)
            throw new IllegalMonitorStateException();
        readers--;
        notifyAll();
    }

    public synchronized void acquireWriteLock() {
        while (readers > 0 || writers > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        writers++;
    }

    public synchronized void releaseWriteLock() {
        if(writers == 0)
            throw new IllegalMonitorStateException();
        writers--;
        notifyAll();
    }
}


