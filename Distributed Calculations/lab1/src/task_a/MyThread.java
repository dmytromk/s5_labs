package task_a;

import javax.swing.*;
import java.lang.Math;

public class MyThread extends Thread {
    private int limit;
    private int direction;
    private static JSlider threadSlider;

    MyThread(JSlider slider, int limit){
        this.threadSlider = slider;
        this.limit = limit;
        this.direction = (int) Math.signum(limit - 50);
    }

    @Override
    public void run(){
        while (!isInterrupted()){
            synchronized (threadSlider) {
                if (threadSlider.getValue() != limit) {
                    threadSlider.setValue((int) (threadSlider.getValue() + Math.signum(limit - threadSlider.getValue())));
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();;
                }
            }
        }
    }
}
