import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class CustomCyclicBarrierTest {

    private CustomCyclicBarrier barrier;
    private int totalParties;
    private int currentParties;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        totalParties = 3; // Adjust this as needed
        barrier = new CustomCyclicBarrier(totalParties);
        currentParties = 0;
    }

    @Test
    public void testConstructorWithCapacity() {
        assertEquals(totalParties, barrier.getParties());
        assertEquals(0, barrier.getNumberWaiting());
    }

    @Test
    public void testConstructorWithCapacityAndCommand() {
        Runnable command = () -> System.out.println("Barrier command executed.");
        barrier = new CustomCyclicBarrier(totalParties, command);
        assertEquals(totalParties, barrier.getParties());
        assertEquals(0, barrier.getNumberWaiting());
    }

    @Test
    public void testAwait() throws InterruptedException {
        Thread[] threads = new Thread[totalParties];

        for (int i = 0; i < totalParties; i++) {
            threads[i] = new Thread(() -> {
                try {
                    barrier.await();
                    synchronized (barrier) {
                        currentParties++;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        assertEquals(totalParties, currentParties);
    }

    @Test
    public void testBarrierWithRunnableCommand() throws InterruptedException {
        Runnable command = () -> System.out.println("Barrier command executed.");
        System.setOut(new PrintStream(outContent));

        barrier = new CustomCyclicBarrier(totalParties, command);

        Thread[] threads = new Thread[totalParties];

        for (int i = 0; i < totalParties; i++) {
            threads[i] = new Thread(() -> {
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        assertEquals("Barrier command executed.\n", outContent.toString());

        System.setOut(originalOut);
    }
}
