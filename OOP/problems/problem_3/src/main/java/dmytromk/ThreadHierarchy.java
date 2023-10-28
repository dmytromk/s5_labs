package dmytromk;

import static java.lang.Thread.interrupted;
import static java.lang.Thread.sleep;

public class ThreadHierarchy {

    public static void displayThreadHierarchy(ThreadGroup group, int indent) {
        Thread[] threads = new Thread[group.activeCount()];
        ThreadGroup[] groups = new ThreadGroup[group.activeGroupCount()];

        group.enumerate(threads, false);
        group.enumerate(groups, false);

        for (int i = 0; i < indent; i++) {
            System.out.print("  ");
        }
        System.out.println("Group: " + group.getName() + ", Parent: " + (group.getParent() != null ? group.getParent().getName() : "N/A"));

        for (Thread thread : threads) {
            if (thread != null) {
                for (int i = 0; i < indent + 1; i++) {
                    System.out.print("  ");
                }
                System.out.println("Thread: " + thread.getName());
            }
        }

        for (ThreadGroup subGroup : groups) {
            if (subGroup != null) {
                displayThreadHierarchy(subGroup, indent + 1);
            }
        }
    }

    public static void main(String[] args) {
        ThreadGroup topLevelGroup = new ThreadGroup("TopLevelGroup");

        ThreadGroup subgroup1 = new ThreadGroup(topLevelGroup, "Subgroup1");
        Thread thread1 = new Thread(subgroup1, () -> {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

        });
        thread1.start();

        ThreadGroup subgroup2 = new ThreadGroup(topLevelGroup, "Subgroup2");
        Thread thread2 = new Thread(subgroup2, () -> {
            try {
                sleep(4000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread2.start();

        Thread hierarchyDisplayThread = new Thread(() -> {
            while(true) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                displayThreadHierarchy(topLevelGroup, 1);
                System.out.println("\n");
            }
        });
        hierarchyDisplayThread.start();
    }
}
