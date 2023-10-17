package task_a;

//Producer
public class Bee implements Runnable {
    private HoneyPot honeyPot;
    private Bear bear;

    public Bee(HoneyPot honeyPot, Bear bear) {
        this.honeyPot = honeyPot;
        this.bear = bear;
    }

    @Override
    public void run() {
        while (bear.isHungry()) {
            this.honeyPot.bringHoney();
            if (this.honeyPot.isFull()) {
                System.out.println("\nPot is full! Wake up the bear!");
                bear.setAsleep(false);
            }
        }
    }
}
