package dmytromk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MichaelScottQueueTest {

    @Test
    public void testEnqueueDequeueSingleThread() {
        MichaelScottQueue<Integer> queue = new MichaelScottQueue<>();

        // Enqueue elements
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        // Dequeue elements
        assertEquals(1, queue.dequeue());
        assertEquals(2, queue.dequeue());
        assertEquals(3, queue.dequeue());
        assertNull(queue.dequeue()); // Queue is empty, expect null
    }

    @Test
    public void testEnqueueDequeueMultipleThreads() throws InterruptedException {
        final MichaelScottQueue<Integer> queue = new MichaelScottQueue<>();
        final int numThreads = 10;
        final int numIterations = 200;

        Thread[] enqueueThreads = new Thread[numThreads];
        Thread[] dequeueThreads = new Thread[numThreads];

        // Enqueue threads
        for (int i = 0; i < numThreads; i++) {
            enqueueThreads[i] = new Thread(() -> {
                for (int j = 0; j < numIterations; j++) {
                    queue.enqueue(j);
                    //System.out.println(j);
                }
            });
            enqueueThreads[i].start();
        }

        for (Thread thread : enqueueThreads) {
            thread.join();
        }

        queue.printQueue();

        // Dequeue threads
        for (int i = 0; i < numThreads; i++) {
            dequeueThreads[i] = new Thread(() -> {
                for (int j = 0; j < numIterations; j++) {
                    Integer value = queue.dequeue();
                    //System.out.println(value);
                }
            });
            dequeueThreads[i].start();
        }

        for (Thread thread : dequeueThreads) {
            thread.join();
        }

        queue.printQueue();

        assertNull(queue.dequeue());
    }
}