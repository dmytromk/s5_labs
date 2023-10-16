package task_b;

public class Producer implements Runnable {
    public Integer produceSpeed = 5;

    @Override
    public void run() {

       while (GlobalSupply.totalSupply > 0) {
           Integer currentLoad = produceSpeed;

           if(GlobalSupply.totalSupply < produceSpeed) {
               currentLoad = GlobalSupply.totalSupply;
               GlobalSupply.totalSupply = 0;
           } else {
               GlobalSupply.totalSupply -= produceSpeed;
           }

           try {
               GlobalSupply.toProduce.put(currentLoad);

               System.out.println("Produced " + currentLoad + " items");
           } catch (InterruptedException e) {
               throw new RuntimeException(e);
           }
       }

        try {
            GlobalSupply.toProduce.put((int) -1.0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
