/**
 * StudentHashTable.java
 * Component owner: T. Asfin Riffath (23DA2-0744)
 *
 * A custom hash table (separate chaining) built from scratch to demonstrate
 * hashing concepts, used for efficient (average O(1)) Student ID lookup.
 */
public class StudentHashTable {

    private static class Node {
        Student data;
        Node next;
        Node(Student data) { this.data = data; }
    }

    private Node[] buckets;
    private int capacity;
    private int count;

    public StudentHashTable() {
        this.capacity = 16;
        this.buckets = new Node[capacity];
    }

    /** Simple hash function: sums character codes of the ID, then mods by capacity. */
    private int hash(String studentId) {
        int hash = 0;
        for (char c : studentId.toUpperCase().toCharArray()) {
            hash = (hash * 31 + c) % capacity;
        }
        return Math.abs(hash);
    }

    /** Inserts (or updates) a student in the hash table, keyed by Student ID. */
    public void put(Student student) {
        int index = hash(student.getStudentId());
        Node current = buckets[index];

        // If ID already exists in this bucket, update it instead of duplicating.
        while (current != null) {
            if (current.data.getStudentId().equalsIgnoreCase(student.getStudentId())) {
                current.data = student;
                return;
            }
            current = current.next;
        }

        Node newNode = new Node(student);
        newNode.next = buckets[index];
        buckets[index] = newNode;
        count++;

        if ((double) count / capacity > 0.75) {
            resize();
        }
    }

    /** Searches for a student by ID. Returns null if not found. Average O(1). */
    public Student get(String studentId) {
        int index = hash(studentId);
        Node current = buckets[index];
        while (current != null) {
            if (current.data.getStudentId().equalsIgnoreCase(studentId)) {
                return current.data;
            }
            current = current.next;
        }
        return null;
    }

    /** Removes a student by ID. Returns true if a record was removed. */
    public boolean remove(String studentId) {
        int index = hash(studentId);
        Node current = buckets[index];
        Node prev = null;
        while (current != null) {
            if (current.data.getStudentId().equalsIgnoreCase(studentId)) {
                if (prev == null) buckets[index] = current.next;
                else prev.next = current.next;
                count--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    /** Doubles capacity and rehashes all entries once the load factor exceeds 0.75. */
    private void resize() {
        Node[] oldBuckets = buckets;
        capacity *= 2;
        buckets = new Node[capacity];
        count = 0;
        for (Node head : oldBuckets) {
            Node current = head;
            while (current != null) {
                put(current.data);
                current = current.next;
            }
        }
    }

    public int getCount() { return count; }
}
