package os;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Manager {
    public Double answer;
    private int secondsLimit = 5;

    private final Map<Double, Double> resultCache = new ConcurrentHashMap<>();

    private currentStatus fStatus = currentStatus.WAITE;
    private currentStatus gStatus = currentStatus.WAITE;
    private final Scanner scanner = new Scanner(System.in);

    public enum currentStatus {
        WAITE, CALCULATION, SUCCESS, FAIL, TIMEOUT, ISNAN, INFINITE
    }

    public void run(Double x) {
        if (resultCache.containsKey(x)) {
            System.out.println("Result for x = " + x + " is cached: " + resultCache.get(x));
            return;
        }

        System.out.println("Calculation started.");
        MyThread threadF = new MyThread("f", x);
        MyThread threadG = new MyThread("g", x);

        Thread menuThread = new Thread(() -> {
            this.menu(threadF, threadG);
        });
        menuThread.start();

        threadF.start();
        threadG.start();
        fStatus = currentStatus.CALCULATION;
        gStatus = currentStatus.CALCULATION;

        try {
            threadF.join(secondsLimit * 1000);
            threadG.join(secondsLimit * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        getFunctionsStatus(threadF, threadG);

        try {
            menuThread.join(); // Wait for the menu thread to finish if desired
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (fStatus==currentStatus.SUCCESS && gStatus==currentStatus.SUCCESS) {
            System.out.println("f(x) = " + threadF.result.getAsDouble());
            System.out.println("g(x) = " + threadG.result.getAsDouble());
            this.answer = calculateAnswer(threadF.result.getAsDouble(),
                    threadG.result.getAsDouble());
            System.out.println("RESULT = " + this.answer);

            resultCache.put(x, this.answer);
        } else System.out.println("Failed to find result.");

//        System.exit(0);
    }

    private Double calculateAnswer(Double... args) {
        Double result = 1.0;
        for (Double arg : args) {
            result *= arg;
        }
        return result;
    }

    public static void interruptThreads(MyThread... threads) {
        for (MyThread thread : threads) {
            if (thread != null) {
                thread.interrupt();
            }
        }
    }

    private void getFunctionsStatus(MyThread threadF, MyThread threadG) {
        if(threadF.isAlive()) {
            threadF.interrupt();
            this.fStatus = currentStatus.TIMEOUT;
        }

        else if(!threadF.calculated) {
            this.fStatus = currentStatus.FAIL;
        }

        else {
            double value = threadF.result.getAsDouble();

            if (Double.isNaN(value)) {
                this.fStatus = currentStatus.ISNAN;
            } else if (Double.isInfinite(value)) {
                this.fStatus = currentStatus.INFINITE;
            } else {
                this.fStatus = currentStatus.SUCCESS;
            }
        }

        if(threadG.isAlive()) {
            threadG.interrupt();
            this.gStatus = currentStatus.TIMEOUT;
        }

        else if(!threadG.calculated) {
            this.gStatus = currentStatus.FAIL;
        }

        else {
            double value = threadG.result.getAsDouble();

            if (Double.isNaN(value)) {
                this.gStatus = currentStatus.ISNAN;
            } else if (Double.isInfinite(value)) {
                this.gStatus = currentStatus.INFINITE;
            } else {
                this.gStatus = currentStatus.SUCCESS;
            }
        }
    }

    private void menu(MyThread... threads) {
        int choice = 1;

        do {
            System.out.println("\n1. Cancel execution");
            System.out.println("2. Function statuses");
            System.out.println("Press any other button to exit this menu");
            System.out.print("Enter your choice: ");
            choice = this.scanner.nextInt();
            this.scanner.nextLine();

            switch (choice) {
                case 1 -> interruptThreads(threads);
                case 2 -> System.out.println("F status: " + fStatus +
                        "\nG status: " + gStatus);
                default -> System.out.println("You exited menu. Goodbye.\n");
            }
        } while (choice == 1 || choice == 2);
    }
}
