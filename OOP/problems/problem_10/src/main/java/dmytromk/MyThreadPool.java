package dmytromk;

import java.util.concurrent.LinkedBlockingQueue;

public class MyThreadPool {
    private final LinkedBlockingQueue<Runnable> queue;
    private final WorkerThread[] workers;

    public MyThreadPool(int poolSize) {
        this.queue = new LinkedBlockingQueue<>();
        this.workers = new WorkerThread[poolSize];

        for (int i = 0; i < poolSize; i++) {
            workers[i] = new WorkerThread(Integer.toString(i));
            workers[i].start();
        }
    }

    public void execute(Runnable command) throws InterruptedException {
        queue.put(command);
    }

    public void shutdown() {
        for (Thread worker : workers) {
            worker.interrupt();
        }
    }

    class WorkerThread extends Thread {
        public WorkerThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // System.out.println("In thread");
                    Runnable task = queue.take();
                    task.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}
