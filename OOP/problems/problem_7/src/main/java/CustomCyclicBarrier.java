public class CustomCyclicBarrier {
    private final int parties;
    private int current;
    private final Runnable barrierCommand;

    public CustomCyclicBarrier(int capacity, Runnable barrierCommand) {
        if (capacity <= 0) throw new IllegalArgumentException();
        this.parties = capacity;
        this.barrierCommand = barrierCommand;
    }

    public CustomCyclicBarrier(int capacity) {
        this(capacity, null);
    }

    public int getNumberWaiting() {
        return this.current;
    }

    public int getParties() {
        return this.parties;
    }

    public synchronized void await() throws InterruptedException {
        current++;
        if (current < parties) {
            wait();
        } else {
            notifyAll();
            current = 0;
            if (barrierCommand != null) {
                barrierCommand.run();
            }
        }
    }
}