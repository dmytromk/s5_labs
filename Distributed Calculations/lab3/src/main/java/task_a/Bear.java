package task_a;

//Consumer
public class Bear implements Runnable {
    private HoneyPot honeyPot;
    private int eatenPots = 0;
    private boolean asleep = true;


    public boolean isHungry() {
        return eatenPots != this.honeyPot.getCapacity();
    }

    public boolean isAsleep() {
        return asleep;
    }

    public synchronized void setAsleep(boolean asleep) {
        this.asleep = asleep;
        notifyAll();
    }

    public Bear(HoneyPot honeyPot) {
        this.honeyPot = honeyPot;
    }

    @Override
    public void run() {
        while (this.isHungry()) {
            synchronized (this) {
                while (this.isAsleep()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                honeyPot.eatHoney();
                this.eatenPots++;
                System.out.println("Current bear fullness: " + this.eatenPots);
                this.setAsleep(true);

            }
        }
    }
}
