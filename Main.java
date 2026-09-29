/**
 * Main.java
 * University Student Record and Campus Route Management System
 * CIT300 - Graded Practical Assignment 1
 *
 * Built by: R. Susana Banu (23DA2-0568), NF. Nadha (23DA2-0569),
 *           T. Asfin Riffath (23DA2-0744), K.N. Suha (23DA2-1120)
 *
 * Menu-driven console interface integrating: Linked List, Stack, Queue,
 * BST, Hash Table, and Graph (BFS/DFS).
 */
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    // Shared data structures across the whole system
    private static StudentLinkedList linkedList = new StudentLinkedList();
    private static ActionStack actionStack = new ActionStack();
    private static ServiceQueue serviceQueue = new ServiceQueue();
    private static StudentBST bst = new StudentBST();
    private static StudentHashTable hashTable = new StudentHashTable();
    private static CampusGraph campusGraph = new CampusGraph();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1:  addStudentRecord(); break;
                case 2:  updateStudentRecord(); break;
                case 3:  deleteStudentRecord(); break;
                case 4:  linkedList.displayAll(); break;
                case 5:  addServiceRequest(); break;
                case 6:  processServiceRequest(); break;
                case 7:  actionStack.displayRecentActions(); break;
                case 8:  bst.displayInOrder(); break;
                case 9:  searchStudentByHashing(); break;
                case 10: addCampusLocation(); break;
                case 11: removeCampusLocation(); break;
                case 12: addCampusConnection(); break;
                case 13: removeCampusConnection(); break;
                case 14: campusGraph.displayConnections(); break;
                case 15: traverseCampus(); break;
                case 16: running = false; System.out.println("Exiting. Goodbye!"); break;
                default: System.out.println("Invalid choice. Please select 1-16.");
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("========================================================");
        System.out.println(" University Student Record & Campus Route Management");
        System.out.println("========================================================");
        System.out.println(" 1. Add Student Record");
        System.out.println(" 2. Update Student Record");
        System.out.println(" 3. Delete Student Record");
        System.out.println(" 4. Display All Records using Linked List");
        System.out.println(" 5. Add Service Request to Queue");
        System.out.println(" 6. Process Next Service Request");
        System.out.println(" 7. Display Recent Actions using Stack");
        System.out.println(" 8. Display Students using BST/AVL");
        System.out.println(" 9. Search Student using Hashing");
        System.out.println("10. Add Campus Location");
        System.out.println("11. Remove Campus Location");
        System.out.println("12. Add Campus Connection/Road");
        System.out.println("13. Remove Campus Connection/Road");
        System.out.println("14. Display Campus Connections");
        System.out.println("15. Traverse Campus Locations using BFS or DFS");
        System.out.println("16. Exit");
        System.out.println("========================================================");
    }

    // ---------- Student record operations (Linked List + BST + Hash Table kept in sync) ----------

    private static void addStudentRecord() {
        String id = readString("Enter Student ID: ");
        String name = readString("Enter Name: ");
        String programme = readString("Enter Programme: ");
        double marks = readDouble("Enter Marks (0-100): ");

        Student student = new Student(id, name, programme, marks);
        if (linkedList.add(student)) {
            bst.insert(student);
            hashTable.put(student);
            actionStack.push("Added student " + id + " (" + name + ")");
            System.out.println("Student record added successfully.");
        }
    }

    private static void updateStudentRecord() {
        String id = readString("Enter Student ID to update: ");
        if (linkedList.search(id) == null) {
            System.out.println("Error: Student not found.");
            return;
        }
        String name = readString("Enter new Name: ");
        String programme = readString("Enter new Programme: ");
        double marks = readDouble("Enter new Marks (0-100): ");

        if (linkedList.update(id, name, programme, marks)) {
            // rebuild BST and hash entries to reflect the update
            Student updated = linkedList.search(id);
            hashTable.put(updated); // put() overwrites existing entry
            actionStack.push("Updated student " + id);
            System.out.println("Student record updated successfully.");
        }
    }

    private static void deleteStudentRecord() {
        String id = readString("Enter Student ID to delete: ");
        Student removed = linkedList.delete(id);
        if (removed == null) {
            System.out.println("Error: Student not found.");
            return;
        }
        bst.delete(id);
        hashTable.remove(id);
        actionStack.push("Deleted student " + id + " (" + removed.getName() + ")");
        System.out.println("Student record deleted successfully.");
    }

    private static void searchStudentByHashing() {
        String id = readString("Enter Student ID to search: ");
        Student student = hashTable.get(id);
        if (student == null) {
            System.out.println("Student not found.");
        } else {
            System.out.println("Found: " + student);
        }
    }

    // ---------- Queue operations ----------

    private static void addServiceRequest() {
        String name = readString("Enter Student Name: ");
        String requestType = readString("Enter Request Type (e.g. Transcript, Appointment): ");
        serviceQueue.enqueue(name + " - " + requestType);
        actionStack.push("Service request added for " + name);
        System.out.println("Service request added to queue.");
    }

    private static void processServiceRequest() {
        String processed = serviceQueue.processNext();
        if (processed != null) {
            actionStack.push("Processed service request: " + processed);
            System.out.println("Processed request: " + processed);
        }
    }

    // ---------- Graph operations ----------

    private static void addCampusLocation() {
        String location = readString("Enter new campus location name: ");
        if (campusGraph.addLocation(location)) {
            actionStack.push("Added campus location: " + location);
            System.out.println("Location added successfully.");
        }
    }

    private static void removeCampusLocation() {
        String location = readString("Enter campus location to remove: ");
        if (campusGraph.removeLocation(location)) {
            actionStack.push("Removed campus location: " + location);
            System.out.println("Location removed successfully.");
        }
    }

  private static void addCampusConnection() {
    String a = readString("Enter first location: ");
    String b = readString("Enter second location: ");

    // If a location is missing, offer to create it instead of failing
    for (String loc : new String[]{a, b}) {
        if (!campusGraph.hasLocation(loc)) {
            String answer = readString("'" + loc + "' does not exist yet. Add it now? (Y/N): ");
            if (answer.equalsIgnoreCase("Y")) {
                if (campusGraph.addLocation(loc)) {
                    actionStack.push("Added campus location: " + loc);
                    System.out.println("Location '" + loc + "' added.");
                }
            } else {
                System.out.println("Connection cancelled.");
                return;
            }
        }
    }

    if (campusGraph.addConnection(a, b)) {
        actionStack.push("Added road: " + a + " <-> " + b);
        System.out.println("Connection added successfully.");
    }
}
    private static void removeCampusConnection() {
        String a = readString("Enter first location: ");
        String b = readString("Enter second location: ");
        if (campusGraph.removeConnection(a, b)) {
            actionStack.push("Removed road: " + a + " <-> " + b);
            System.out.println("Connection removed successfully.");
        }
    }

    private static void traverseCampus() {
        if (campusGraph.getAllLocations().isEmpty()) {
            System.out.println("No campus locations to traverse.");
            return;
        }
        String start = readString("Enter starting location: ");
        String type = readString("Traverse using BFS or DFS? (enter B or D): ");
        if (type.equalsIgnoreCase("B")) {
            campusGraph.bfs(start);
        } else if (type.equalsIgnoreCase("D")) {
            campusGraph.dfs(start);
        } else {
            System.out.println("Invalid traversal type selected.");
        }
    }

    // ---------- Input helper methods with validation ----------

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value < 0 || value > 100) {
                    System.out.println("Marks must be between 0 and 100. Try again.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}
