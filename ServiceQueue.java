/**
 * ServiceQueue.java
 * Component owner: NF. Nadha (23DA2-0569)
 *
 * A queue (FIFO) used to manage student service requests in the order
 * they arrive (e.g. transcript requests, appointment requests).
 */
public class ServiceQueue {

    private static class Node {
        String request;
        Node next;
        Node(String request) { this.request = request; }
    }

    private Node front;
    private Node rear;
    private int size;

    /** Adds a new service request to the back of the queue. */
    public void enqueue(String request) {
        Node newNode = new Node(request);
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    /** Removes and returns the request at the front of the queue. Returns null if empty. */
    public String processNext() {
        if (isEmpty()) {
            System.out.println("No pending service requests.");
            return null;
        }
        String request = front.request;
        front = front.next;
        if (front == null) rear = null;
        size--;
        return request;
    }

    public boolean isEmpty() { return front == null; }
    public int getSize() { return size; }

    /** Displays all pending requests in order of arrival, without removing them. */
    public void displayQueue() {
        if (isEmpty()) {
            System.out.println("No pending service requests.");
            return;
        }
        System.out.println("--- Pending Service Requests (Queue, FIFO order) ---");
        Node current = front;
        int position = 1;
        while (current != null) {
            System.out.println(position + ". " + current.request);
            current = current.next;
            position++;
        }
    }
}
