package task_b;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

public class StringThreadsManager {
    private int threadsAmount;
    private int minCorrectAmount;
    private Map<Integer, Integer> correctLetters;


    public StringThreadsManager(int threadsAmount, int minCorrectAmount){
        this.threadsAmount = threadsAmount;
        this.minCorrectAmount = minCorrectAmount;
        this.correctLetters = new HashMap<>(threadsAmount);
    }

    public void insert(int id, int value) {
        correctLetters.put(id, value);
    }

    public boolean checkCorrect() {
        Map<Integer, Integer> valueOccurrences = new HashMap<>();

        for (Integer value : correctLetters.values()) {
            valueOccurrences.put(value, valueOccurrences.getOrDefault(value, 0) + 1);
        }

        // Check if any value has at least three occurrences
        for (Integer count : valueOccurrences.values()) {
            if (count >= 3) {
                return true;
            }
        }

        return false;
    }

    public void start() {
        CyclicBarrier barrier = new CyclicBarrier(threadsAmount);
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < threadsAmount; i++) {
            StringThread stringThread = new StringThread(i, barrier, this);
            Thread thread = new Thread(stringThread);
            thread.start();
            threadList.add(thread);
        }

        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
