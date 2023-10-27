package task_b;

import task_b.StringThreadsManager;

public class Main {
    public static void main(String[] args) {
        StringThreadsManager stringThreadsManager =
                new StringThreadsManager(4, 3);

        stringThreadsManager.start();
    }
}