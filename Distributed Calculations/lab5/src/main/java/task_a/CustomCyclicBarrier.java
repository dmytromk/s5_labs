package task_a;

public class CustomCyclicBarrier {
    private int capacity;
    private int current;
    private final Runnable callback;

    public CustomCyclicBarrier(int capacity) {
        this.capacity = capacity;
        this.callback = null;
    }

    public CustomCyclicBarrier(int capacity, Runnable callback) {
        this.capacity = capacity;
        this.callback = callback;
    }

    public synchronized void await() throws InterruptedException {
        current++;
        if (current < capacity) {
            wait();
        } else {
            notifyAll();
            current = 0;
            if (callback != null) {
                callback.run();
            }
        }
    }
}