package task_b;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class GlobalSupply {
    public static int totalSupply = 50;
    public static int totalResult = 0;
    public static BlockingQueue<Integer> toProduce = new ArrayBlockingQueue<>(50); //Producer -> ProducerConsumer
    public static BlockingQueue<Integer> toConsume = new ArrayBlockingQueue<>(50); //ProducerConsumer -> Consumer
}
