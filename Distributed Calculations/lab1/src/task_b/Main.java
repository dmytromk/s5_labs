package task_b;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static AtomicInteger semaphore = new AtomicInteger(0);
    private static task_b.MyThread threadLeft;
    private static task_b.MyThread threadRight;


    private static JFrame window;
    private static JSlider slider;
    private static JButton startLeftButton;
    private static JButton stopLeftButton;
    private static JButton startRightButton;
    private static JButton stopRightButton;
    private static JLabel blockerLabel;

    private static JPanel panel;
    private static JPanel buttonsPanel;
    private static JPanel buttonsLeftPanel;
    private static JPanel buttonsRightPanel;


    private static void startUI(){
        window = new JFrame();
        window.setSize(500, 500);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        slider = new JSlider();
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        startLeftButton = new JButton("Start Left");
        startLeftButton.addActionListener(e -> startLeft());
        stopLeftButton = new JButton("Stop Left");
        stopLeftButton.addActionListener(e -> stopLeft());
        stopLeftButton.setEnabled(false);

        startRightButton = new JButton("Start Right");
        startRightButton.addActionListener(e -> startRight());
        stopRightButton = new JButton("Stop Right");
        stopRightButton.addActionListener(e -> stopRight());
        stopRightButton.setEnabled(false);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        buttonsLeftPanel = new JPanel();
        buttonsLeftPanel.setLayout(new BoxLayout(buttonsLeftPanel, BoxLayout.X_AXIS));
        buttonsLeftPanel.add(startLeftButton);
        buttonsLeftPanel.add(stopLeftButton);

        buttonsRightPanel = new JPanel();
        buttonsRightPanel.setLayout(new BoxLayout(buttonsRightPanel, BoxLayout.X_AXIS));
        buttonsRightPanel.add(startRightButton);
        buttonsRightPanel.add(stopRightButton);

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.add(buttonsLeftPanel);
        buttonsPanel.add(buttonsRightPanel);

        blockerLabel = new JLabel("Threads are blocked");
        blockerLabel.setVisible(false);
        
        panel.add(slider);
        panel.add(buttonsPanel);
        panel.add(blockerLabel);

        window.setContentPane(panel);
        window.setVisible(true);
    }


    private static void startLeft(){
        if(semaphore.get() == 0) {
            threadLeft = new MyThread(slider, 10);
            threadLeft.setDaemon(true);
            threadLeft.start();
            startRightButton.setEnabled(false);
            stopRightButton.setEnabled(false);
            blockerLabel.setVisible(true);
            stopLeftButton.setEnabled(true);
        }
    }

    private static void startRight(){
        if(semaphore.get() == 0) {
            threadRight = new MyThread(slider, 90);
            threadRight.setDaemon(true);
            threadRight.start();
            startLeftButton.setEnabled(false);
            stopRightButton.setEnabled(true);
            blockerLabel.setVisible(true);
        }
    }

    private static void stopLeft() {
        threadLeft.interrupt();
        startRightButton.setEnabled(true);
        stopLeftButton.setEnabled(false);
        blockerLabel.setVisible(false);
    }

    private static void stopRight() {
        threadRight.interrupt();
        startLeftButton.setEnabled(true);
        stopRightButton.setEnabled(false);
        blockerLabel.setVisible(false);
    }


    public static void main(String[] args) {
        startUI();
    }
}
