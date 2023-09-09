package task_a;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setSize(500, 500);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JSlider slider = new JSlider();
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        MyThread threadLeft = new MyThread(slider, 10);
        MyThread threadRight = new MyThread(slider, 90);

        threadLeft.setDaemon(true);
        threadRight.setDaemon(true);

        SpinnerModel leftModel = new SpinnerNumberModel(Thread.NORM_PRIORITY, Thread.MIN_PRIORITY, Thread.MAX_PRIORITY, 1);
        JSpinner leftSpinner = new JSpinner(leftModel);
        leftSpinner.addChangeListener(e -> {
            int changedValue = (int) leftSpinner.getValue();
            threadLeft.setPriority(changedValue);
        });

        SpinnerModel rightModel = new SpinnerNumberModel(Thread.NORM_PRIORITY, Thread.MIN_PRIORITY, Thread.MAX_PRIORITY, 1);
        JSpinner rightSpinner = new JSpinner(rightModel);
        rightSpinner.addChangeListener(e -> {
            int changedValue = (int) rightSpinner.getValue();
            threadRight.setPriority(changedValue);
        });

        panel.add(slider);

        JPanel spinnerPanel = new JPanel();
        spinnerPanel.add(leftSpinner);
        spinnerPanel.add(rightSpinner);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            threadLeft.start();
            threadRight.start();
        });
        JPanel startPanel = new JPanel();
        startPanel.add(startButton);

        panel.add(spinnerPanel);
        panel.add(startPanel);

        window.setContentPane(panel);
        window.setVisible(true);
    }
}