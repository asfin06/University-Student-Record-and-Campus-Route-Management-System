/**
 * ActionStack.java
 * Component owner: NF. Nadha (23DA2-0569)
 *
 * A stack (LIFO) used to record recent actions performed on student records
 * (add / update / delete), giving a simple history / undo-style log.
 */
import java.util.EmptyStackException;

public class ActionStack {

    private static class Node {
        String action;
        Node next;
        Node(String action) { this.action = action; }
    }

    private Node top;
    private int size;

    /** Pushes a new action description onto the stack. */
    public void push(String action) {
        Node newNode = new Node(action);
        newNode.next = top;
        top = newNode;
        size++;
    }

    /** Pops (removes and returns) the most recent action. */
    public String pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        String action = top.action;
        top = top.next;
        size--;
        return action;
    }

    /** Peeks at the most recent action without removing it. */
    public String peek() {
        if (isEmpty()) return null;
        return top.action;
    }

    public boolean isEmpty() { return top == null; }
    public int getSize() { return size; }

    /** Displays all recorded actions, most recent first. */
    public void displayRecentActions() {
        if (isEmpty()) {
            System.out.println("No recent actions recorded.");
            return;
        }
        System.out.println("--- Recent Actions (Stack, most recent first) ---");
        Node current = top;
        int count = 1;
        while (current != null) {
            System.out.println(count + ". " + current.action);
            current = current.next;
            count++;
        }
    }
}
