package task_b;

public class Consumer implements Runnable{

    @Override
    public void run() {
        Integer currentCounter = 0;

        try {
            while (true) {
                currentCounter = GlobalSupply.toConsume.take();

                if(currentCounter < 0) {
                    return;
                }

                System.out.println("Calculated " + currentCounter + " items");
                GlobalSupply.totalResult += currentCounter;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
