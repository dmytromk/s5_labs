package task_a;

import task_b.StringThread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class RecruitSimulator {
    private int numberOfRecruits;
    private int numThreads;
    private int[] recruits; //0 - left, 1 - right
    private boolean finished;
    Random random;

    public RecruitSimulator(int numberOfRecruits, int numThreads) {
        this.numberOfRecruits = numberOfRecruits;
        this.numThreads = numThreads;
        this.recruits = new int[numberOfRecruits];
        this.finished = false;
        this.random = new Random();

        for (int i = 0; i < numberOfRecruits; i++) {
            recruits[i] = random.nextInt(2);
        }
    }

    private class CheckOrder implements Runnable {
        @Override
        public void run() {
            //System.out.println("");
            System.out.println(Arrays.toString(recruits) + "\n");
            for (int i = 0; i < numberOfRecruits-1; i++) {
                if (recruits[i] != recruits[i+1]) {
                    return;
                }
            }
            finished = true;
        }
    }

    private class RecruitGoup implements Runnable {
        private final int id;
        private final int leftBoundIndex; //inclusive
        private final int rightBoundIndex; //exclusive
        CustomCyclicBarrier cyclicBarrier;

        public RecruitGoup(int id, CustomCyclicBarrier cyclicBarrier) {
            this.id = id;
            this.leftBoundIndex = (numberOfRecruits / numThreads) * id;
            this.rightBoundIndex = (numberOfRecruits / numThreads) * id + (numberOfRecruits / numThreads);
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if(finished){
                    break;
                }

                for (int i = leftBoundIndex; i < rightBoundIndex; i++) {
                    recruits[i] = random.nextInt(2); //recruit[i] turns to new random direction

                    switch (recruits[i]) {
                        case 0 -> {
                            if (i != leftBoundIndex && recruits[i] != recruits[i-1]) {
                                recruits[i] = 1 - recruits[i];
                                recruits[i-1] = 1 - recruits[i-1];
                            }
                        }
                        case 1 -> {
                            if (i != rightBoundIndex-1 && recruits[i] != recruits[i+1]) {
                                recruits[i] = 1 - recruits[i];
                                recruits[i+1] = 1 - recruits[i+1];
                            }
                        }
                    }

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void start() {
        CustomCyclicBarrier barrier = new CustomCyclicBarrier(numThreads, new CheckOrder());
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            Thread thread = new Thread(new RecruitGoup(i, barrier));
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

    public static void main(String[] args) {
        RecruitSimulator recruitSimulator = new RecruitSimulator(6, 2);
        recruitSimulator.start();
    }
}
