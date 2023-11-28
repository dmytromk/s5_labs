package dmytromk;

import java.util.concurrent.atomic.AtomicReference;

public class MichaelScottQueue<T> {
    private static class Node<T> {
        public final T value;
        public final AtomicReference<Node<T>> next;

        public Node(T value) {
            this.value = value;
            this.next = new AtomicReference<>(null);
        }

        public Node(T value, Node<T> next) {
            this.value = value;
            this.next = new AtomicReference<>(next);
        }
    }

    private AtomicReference<Node<T>> head;
    private AtomicReference<Node<T>> tail;

    public MichaelScottQueue() {
        Node<T> dummy = new Node<>(null);
        head = new AtomicReference<>(dummy);
        tail = new AtomicReference<>(dummy);
    }

    public void enqueue(T value) {
        Node<T> node = new Node<>(value);
        AtomicReference<Node<T>> currentTail;

        while (true) {
            currentTail = tail;
            AtomicReference<Node<T>> next = currentTail.get().next;

            if (currentTail.get() == tail.get()) {
                if (next.get() == null) {
                    if (currentTail.get().next.compareAndSet(null, node))
                        break;
                } else {
                    tail.compareAndSet(currentTail.get(), next.get());
                }
            }
        }

        tail.compareAndSet(currentTail.get(), node);
    }

    public T dequeue() {
        while (true) {
            Node<T> headNode = head.get();
            Node<T> tailNode = tail.get();
            Node<T> nextNode = headNode.next.get();

            if (headNode == head.get()) {
                //is queue empty or is tail falling behind?
                if (headNode == tailNode) {
                    if (nextNode == null) {
                        //Queue is empty
                        return null;
                    }
                    //Tail is falling behind, advance it
                    tail.compareAndSet(tailNode, nextNode);
                } else {
                    T returnedValue = nextNode.value;
                    // Try to swing Head to the next node
                    if (head.compareAndSet(headNode, nextNode))
                        return returnedValue;
                }
            }
        }
    }

    public void printQueue() {
        Node<T> current = head.get().next.get(); // Start from the first actual node (skip dummy node)
        while (current != null) {
            System.out.print(current.value + " ");
            current = current.next.get();
        }
        System.out.println(); // Add a newline for better formatting
    }
}
