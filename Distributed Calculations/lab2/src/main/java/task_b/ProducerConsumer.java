package task_b;

public class ProducerConsumer implements Runnable{

    @Override
    public void run() {
        while (true) {
            Integer currentLoaded = 0;
            try {
                currentLoaded = GlobalSupply.toProduce.take();

                if(currentLoaded < 0){
                    GlobalSupply.toConsume.put((int) -1.0);
                    return;
                }

                System.out.println("Loaded " + currentLoaded + " items");
                GlobalSupply.toConsume.put(currentLoaded);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
