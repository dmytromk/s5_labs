package task_a;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        HoneyPot honeyPot = new HoneyPot(3);

        Bear bear = new Bear(honeyPot);
        int numOfBees = 2;

        List<Bee> bees = new ArrayList<>(numOfBees);
        for(int i =0 ; i < numOfBees; i++){
            bees.add(new Bee(honeyPot, bear));
        }

        Thread threadBear = new Thread(bear);
        threadBear.start();

        for(int i = 0 ; i < numOfBees; i++){
            Bee bee = bees.get(i);
            Thread threadBee = new Thread(bee);
            threadBee.start();
        }
    }
}
