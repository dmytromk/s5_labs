package task_a;

public class HoneyPot {
    private int capacity;
    private int currentHoney;

    public HoneyPot(int capacity) {
        this.capacity = capacity;
        this.currentHoney = 0;
    }

    public int getCapacity() {
        return capacity;
    }

    public synchronized void eatHoney() {
        this.currentHoney--;
        System.out.println("Winnie the Pooh ate 1 cup of honey!");
        notifyAll();
    }

    public synchronized void bringHoney() {
        while (this.isFull())
        {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        this.currentHoney++;
        System.out.println("Bee brought 1 cup of honey.");
        if (this.isFull()) {
            notify();
        }
    }

    public synchronized boolean isFull() {
        return this.currentHoney == this.capacity;
    }
}
