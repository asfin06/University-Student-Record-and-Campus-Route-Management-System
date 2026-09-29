/**
 * CampusGraph.java
 * Component owner: K.N. Suha (23DA2-1120)
 *
 * Represents the university campus as an undirected graph using an
 * adjacency list. Vertices = campus locations, edges = roads/paths.
 * Supports add/remove location, add/remove connection, display,
 * and BFS / DFS traversal.
 */
import java.util.*;

public class CampusGraph {

    // key = lowercase name, value = connected lowercase names
    private Map<String, LinkedHashSet<String>> adjList = new LinkedHashMap<>();
    // key = lowercase name, value = original name (for display)
    private Map<String, String> displayNames = new HashMap<>();

    private String key(String location) {
        return location.trim().toLowerCase().replaceAll("\\s+", " ");
    }

    public boolean addLocation(String location) {
        String k = key(location);
        if (k.isEmpty()) {
            System.out.println("Error: Location name cannot be empty.");
            return false;
        }
        if (adjList.containsKey(k)) {
            System.out.println("Error: Location already exists.");
            return false;
        }
        adjList.put(k, new LinkedHashSet<>());
        displayNames.put(k, location.trim().replaceAll("\\s+", " "));
        return true;
    }

    public boolean removeLocation(String location) {
        String k = key(location);
        if (!adjList.containsKey(k)) {
            System.out.println("Error: Location not found.");
            System.out.println("Available locations: " + getDisplayList());
            return false;
        }
        adjList.remove(k);
        displayNames.remove(k);
        for (Set<String> neighbours : adjList.values()) {
            neighbours.remove(k);
        }
        return true;
    }

    public boolean addConnection(String locationA, String locationB) {
        String a = key(locationA);
        String b = key(locationB);
        if (!adjList.containsKey(a) || !adjList.containsKey(b)) {
            System.out.println("Error: One or both locations do not exist.");
            System.out.println("Available locations: " + getDisplayList());
            return false;
        }
        if (a.equals(b)) {
            System.out.println("Error: Cannot connect a location to itself.");
            return false;
        }
        if (adjList.get(a).contains(b)) {
            System.out.println("Error: These locations are already connected.");
            return false;
        }
        adjList.get(a).add(b);
        adjList.get(b).add(a);
        return true;
    }

    public boolean removeConnection(String locationA, String locationB) {
        String a = key(locationA);
        String b = key(locationB);
        if (!adjList.containsKey(a) || !adjList.containsKey(b)) {
            System.out.println("Error: One or both locations do not exist.");
            System.out.println("Available locations: " + getDisplayList());
            return false;
        }
        boolean removed = adjList.get(a).remove(b);
        adjList.get(b).remove(a);
        if (!removed) {
            System.out.println("Error: No existing connection between these locations.");
        }
        return removed;
    }

    public void displayConnections() {
        if (adjList.isEmpty()) {
            System.out.println("No campus locations added yet.");
            return;
        }
        System.out.println("--- Campus Network (Adjacency List) ---");
        for (String k : adjList.keySet()) {
            List<String> names = new ArrayList<>();
            for (String n : adjList.get(k)) names.add(displayNames.get(n));
            System.out.println(displayNames.get(k) + " -> " + names);
        }
    }

    public void bfs(String start) {
        String s = key(start);
        if (!adjList.containsKey(s)) {
            System.out.println("Error: Starting location not found.");
            System.out.println("Available locations: " + getDisplayList());
            return;
        }
        Set<String> visited = new LinkedHashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(s);
        visited.add(s);

        System.out.print("BFS Traversal from " + displayNames.get(s) + ": ");
        while (!queue.isEmpty()) {
            String current = queue.poll();
            System.out.print(displayNames.get(current) + " ");
            for (String neighbour : adjList.get(current)) {
                if (!visited.contains(neighbour)) {
                    visited.add(neighbour);
                    queue.add(neighbour);
                }
            }
        }
        System.out.println();
    }

    public void dfs(String start) {
        String s = key(start);
        if (!adjList.containsKey(s)) {
            System.out.println("Error: Starting location not found.");
            System.out.println("Available locations: " + getDisplayList());
            return;
        }
        Set<String> visited = new LinkedHashSet<>();
        System.out.print("DFS Traversal from " + displayNames.get(s) + ": ");
        dfsRec(s, visited);
        System.out.println();
    }

    private void dfsRec(String current, Set<String> visited) {
        visited.add(current);
        System.out.print(displayNames.get(current) + " ");
        for (String neighbour : adjList.get(current)) {
            if (!visited.contains(neighbour)) {
                dfsRec(neighbour, visited);
            }
        }
    }

    private List<String> getDisplayList() {
        return new ArrayList<>(displayNames.values());
    }

    public boolean hasLocation(String location) {
        return adjList.containsKey(key(location));
    }

    public Set<String> getAllLocations() {
        return adjList.keySet();
    }
}