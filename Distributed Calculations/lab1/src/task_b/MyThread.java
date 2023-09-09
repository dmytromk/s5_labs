package task_b;

import javax.swing.*;

public class MyThread extends Thread {
    private int limit;
    private static JSlider threadSlider;

    MyThread(JSlider slider, int limit){
        MyThread.threadSlider = slider;
        this.limit = limit;
    }

    @Override
    public void run(){
        if(Main.semaphore.compareAndSet(0, 1)) {
            while (!isInterrupted()) {
                threadSlider.setValue((int) (limit));
            };
            Main.semaphore.set(0);
        }
    }
}

