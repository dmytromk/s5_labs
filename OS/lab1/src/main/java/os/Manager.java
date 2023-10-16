package os;

import java.util.ArrayList;

public class Manager {
    public Double answer;

    private int secondsLimit = 5;

    private currentStatus fStatus = currentStatus.WAITE;
    private currentStatus gStatus = currentStatus.WAITE;
    public enum currentStatus {
        WAITE, CALCULATION, SUCCESS, FAIL, TIMEOUT, ISNAN, INFINITE
    }

    public void run(Double x) {
        System.out.println("Calculation started. Press any button to exit the program.");
        MyThread threadF = new MyThread("f", x);
        MyThread threadG = new MyThread("g", x);
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
        System.out.println("F status: " + fStatus);
        System.out.println("G status: " + gStatus);

        if (fStatus==currentStatus.SUCCESS && gStatus==currentStatus.SUCCESS) {
            System.out.println("f(x) = " + threadF.result.getAsDouble());
            System.out.println("g(x) = " + threadG.result.getAsDouble());
            this.answer = calculateAnswer(threadF.result.getAsDouble(),
                    threadG.result.getAsDouble());
            System.out.println("RESULT = " + this.answer);
        } else System.out.println("Failed to find result.");

        System.exit(0);
    }

    private Double calculateAnswer(Double... args) {
        Double result = 1.0;
        for (Double arg : args) {
            result *= arg;
        }
        return result;
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
}
