/**
 * StudentLinkedList.java
 * Component owner: R. Susana Banu (23DA2-0568)
 *
 * A singly linked list used as the primary store for student records.
 * Supports add, update, delete, search, and display with input validation
 * (duplicate ID rejection, invalid marks rejection, missing-record handling).
 */
public class StudentLinkedList {

    private static class Node {
        Student data;
        Node next;
        Node(Student data) { this.data = data; }
    }

    private Node head;
    private int size;

    /** Adds a new student. Returns false if the ID already exists or marks are invalid. */
    public boolean add(Student student) {
        if (student.getMarks() < 0 || student.getMarks() > 100) {
            System.out.println("Error: Marks must be between 0 and 100.");
            return false;
        }
        if (search(student.getStudentId()) != null) {
            System.out.println("Error: Duplicate Student ID. Record not added.");
            return false;
        }
        Node newNode = new Node(student);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) current = current.next;
            current.next = newNode;
        }
        size++;
        return true;
    }

    /** Searches for a student by ID. Returns null if not found. */
    public Student search(String studentId) {
        Node current = head;
        while (current != null) {
            if (current.data.getStudentId().equalsIgnoreCase(studentId)) {
                return current.data;
            }
            current = current.next;
        }
        return null;
    }

    /** Updates an existing student's name, programme, and marks. */
    public boolean update(String studentId, String name, String programme, double marks) {
        if (marks < 0 || marks > 100) {
            System.out.println("Error: Marks must be between 0 and 100.");
            return false;
        }
        Student student = search(studentId);
        if (student == null) {
            System.out.println("Error: Student not found.");
            return false;
        }
        student.setName(name);
        student.setProgramme(programme);
        student.setMarks(marks);
        return true;
    }

    /** Deletes a student by ID. Returns the removed student, or null if not found. */
    public Student delete(String studentId) {
        if (head == null) return null;

        if (head.data.getStudentId().equalsIgnoreCase(studentId)) {
            Student removed = head.data;
            head = head.next;
            size--;
            return removed;
        }

        Node current = head;
        while (current.next != null) {
            if (current.next.data.getStudentId().equalsIgnoreCase(studentId)) {
                Student removed = current.next.data;
                current.next = current.next.next;
                size--;
                return removed;
            }
            current = current.next;
        }
        return null;
    }

    /** Displays all student records in insertion order. */
    public void displayAll() {
        if (head == null) {
            System.out.println("No student records found.");
            return;
        }
        System.out.println("--- All Student Records (Linked List) ---");
        Node current = head;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }

    public int getSize() { return size; }

    /** Returns all students as an array-like iteration helper for other modules (BST/Hash rebuild). */
    public java.util.List<Student> getAllStudents() {
        java.util.List<Student> list = new java.util.ArrayList<>();
        Node current = head;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }
}
