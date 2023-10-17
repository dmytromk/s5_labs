package task_b;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Semaphore;

public class Barber {
    private Semaphore semaphore = new Semaphore(1);
    private Queue<Client> clients = new ConcurrentLinkedDeque<>();

    public void addClientToQueue(Client client) {
        clients.add(client);
        System.out.println(Thread.currentThread().getName() + " entered the queue.");
        cutHair();
    }

    public void cutHair() {
        try {
            semaphore.acquire();
            System.out.println(Thread.currentThread().getName() + " sat down in the chair.");
            clients.remove();
            System.out.println(Thread.currentThread().getName() + " exited barbershop.");
            semaphore.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
