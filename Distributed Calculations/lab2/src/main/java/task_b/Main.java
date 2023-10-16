package task_b;

public class Main {
    public static void main(String[] args) {

        Thread producer = new Thread(new Producer());
        Thread producerCustomer = new Thread(new ProducerConsumer());
        Thread Customer = new Thread(new Consumer());

        System.out.println("START: " + GlobalSupply.totalSupply + " items\n");

        producerCustomer.start();
        Customer.start();
        producer.start();
    }
}
