/**
 * StudentBST.java
 * Component owner: T. Asfin Riffath (23DA2-0744)
 *
 * A Binary Search Tree that organizes student records by Student ID,
 * allowing sorted (in-order) display and efficient search (O(log n) average).
 */
public class StudentBST {

    private static class TreeNode {
        Student data;
        TreeNode left, right;
        TreeNode(Student data) { this.data = data; }
    }

    private TreeNode root;

    /** Inserts a student into the BST, keyed by Student ID. */
    public void insert(Student student) {
        root = insertRec(root, student);
    }

    private TreeNode insertRec(TreeNode node, Student student) {
        if (node == null) return new TreeNode(student);

        int cmp = student.getStudentId().compareToIgnoreCase(node.data.getStudentId());
        if (cmp < 0) {
            node.left = insertRec(node.left, student);
        } else if (cmp > 0) {
            node.right = insertRec(node.right, student);
        }
        // if cmp == 0 (duplicate ID), ignore — duplicates are already blocked in the linked list
        return node;
    }

    /** Removes a student from the BST by Student ID. */
    public boolean delete(String studentId) {
        int sizeBefore = countNodes(root);
        root = deleteRec(root, studentId);
        return countNodes(root) < sizeBefore;
    }

    private TreeNode deleteRec(TreeNode node, String studentId) {
        if (node == null) return null;

        int cmp = studentId.compareToIgnoreCase(node.data.getStudentId());
        if (cmp < 0) {
            node.left = deleteRec(node.left, studentId);
        } else if (cmp > 0) {
            node.right = deleteRec(node.right, studentId);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            TreeNode successor = findMin(node.right);
            node.data = successor.data;
            node.right = deleteRec(node.right, successor.data.getStudentId());
        }
        return node;
    }

    private TreeNode findMin(TreeNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private int countNodes(TreeNode node) {
        if (node == null) return 0;
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    /** Searches for a student by ID. Returns null if not found. */
    public Student search(String studentId) {
        TreeNode current = root;
        while (current != null) {
            int cmp = studentId.compareToIgnoreCase(current.data.getStudentId());
            if (cmp == 0) return current.data;
            current = (cmp < 0) ? current.left : current.right;
        }
        return null;
    }

    /** Displays all students sorted by Student ID (in-order traversal). */
    public void displayInOrder() {
        if (root == null) {
            System.out.println("No student records in the tree.");
            return;
        }
        System.out.println("--- Students Sorted by ID (BST In-Order) ---");
        inOrderRec(root);
    }

    private void inOrderRec(TreeNode node) {
        if (node == null) return;
        inOrderRec(node.left);
        System.out.println(node.data);
        inOrderRec(node.right);
    }
}
