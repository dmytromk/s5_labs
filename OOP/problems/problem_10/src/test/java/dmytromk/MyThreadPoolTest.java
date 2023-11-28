package dmytromk;

class MyThreadPoolTest {

    @org.junit.jupiter.api.Test
    void test() throws InterruptedException {
        MyThreadPool threadPool = new MyThreadPool(3);

        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            threadPool.execute(() -> {
                System.out.println("Task " + taskId + " is running in thread " + Thread.currentThread().getName());
            });
        }

        Thread.sleep(2000);

        threadPool.shutdown();
    }
}