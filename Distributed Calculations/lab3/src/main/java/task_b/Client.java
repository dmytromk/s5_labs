package task_b;

public class Client implements Runnable {
    private Barber barber;

    public Client(Barber barber) {
        this.barber = barber;
    }

    @Override
    public void run() {
        barber.addClientToQueue(this);
    }
}
