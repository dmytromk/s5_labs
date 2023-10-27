package task_b;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class StringThread implements Runnable {
    private final int id;
    private String string;
    private int correctLettersCount;
    private final CyclicBarrier barrier;
    private final StringThreadsManager stringThreadsManager;
    private final Random random = new Random();


    StringThread(int id, CyclicBarrier barrier, StringThreadsManager stringThreadsManager) {
        this.id = id;
        this.string = StringManipulator.generateRandomString(5);
        this.barrier = barrier;
        this.correctLettersCount = StringManipulator.countCorrectLetters(this.string);
        this.stringThreadsManager = stringThreadsManager;
    }

    @Override
    public void run() {
        while (true) {
            this.string = StringManipulator.randomCharTransform(string);
            int abCount = StringManipulator.countCorrectLetters(string);
            this.correctLettersCount = abCount;
            stringThreadsManager.insert(id, abCount);
            try {
                barrier.await();
                System.out.println("Thread: " + id + "; " + string + "; " + correctLettersCount);
                if (stringThreadsManager.checkCorrect()) {
                    return;
                }
                barrier.await();
                if (id == 0) {
                    System.out.println("\n");
                }
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
