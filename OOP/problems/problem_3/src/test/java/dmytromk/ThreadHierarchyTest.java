package dmytromk;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static dmytromk.ThreadHierarchy.displayThreadHierarchy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ThreadHierarchyTest {

    @Test
    void testDisplayThreadHierarchy() {
        ThreadGroup rootGroup = new ThreadGroup("Root");
        Thread thread1 = new Thread(rootGroup, () -> {
            while (true) {}
        }, "Thread 1");
        thread1.start();

        ThreadGroup subGroup = new ThreadGroup(rootGroup, "Subgroup");
        Thread thread2 = new Thread(subGroup, () -> {
            while (true) {}
        }, "Thread 2");
        thread2.start();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Thread hierarchyDisplayThread = new Thread(() -> {
            displayThreadHierarchy(rootGroup, 0);

            System.setOut(System.out);

            String expectedOutput = "Group: Root, Parent: main\n" +
                    " Thread: Thread 1\n" +
                    "  Group: Subgroup, Parent: Root\n" +
                    "    Thread: Thread 2\n";

            assertEquals(expectedOutput, outputStream.toString());
        });

        hierarchyDisplayThread.start();
    }
}