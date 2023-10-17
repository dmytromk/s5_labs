package task_b;

public class Main {
    public static void main(String[] args) {
        int numberOfVisitors = 5;

        Thread[] threads = new Thread[numberOfVisitors];
        Barber barber = new Barber();

        for (int i = 0; i < numberOfVisitors; i++) {
            threads[i] = new Thread(new Client(barber), "Client №" + i);
            threads[i].start();
        }

        for (int i =0; i < numberOfVisitors; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
