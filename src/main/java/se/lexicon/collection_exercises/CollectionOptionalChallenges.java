package se.lexicon.collection_exercises;

import java.util.*;

/**
 * Solutions for the 5 Optional Challenges.
 * Each method starts with the exercise question as a comment,
 * followed by a clean implementation with explanations.
 */
public class CollectionOptionalChallenges {

    void main() {
        optional1_RotateList();
        optional2_LongestConsecutiveSequence();
        optional3_LruCache();
        optional4_GraphAdjacencyList();
        optional5_SublistWithTargetSum();
    }

    // ─────────────────────────────────────────────────────────────
    // Optional 1 – Rotate a List
    //
    // Given a list and a number k, rotate the list to the RIGHT by k positions.
    // Input:  [1, 2, 3, 4, 5],  k = 2
    // Output: [4, 5, 1, 2, 3]
    //
    // Think about it: what happens when k is larger than the list size?
    // ─────────────────────────────────────────────────────────────
    static void optional1_RotateList() {
        System.out.println("\n=== Optional 1 – Rotate a List ===");

        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        int k = 2;
        int n = list.size();

        // k % n handles the case where k >= n (rotating by n is a full cycle = no change)
        k = k % n;

        // Slice the last k elements, then prepend them to the rest
        List<Integer> tail = new ArrayList<>(list.subList(n - k, n));
        List<Integer> head = new ArrayList<>(list.subList(0, n - k));

        list.clear();
        list.addAll(tail);
        list.addAll(head);

        System.out.println("Result: " + list);
    }

    // ─────────────────────────────────────────────────────────────
    // Optional 2 – Longest Consecutive Sequence
    //
    // Given an unsorted list of integers, find the LENGTH of the longest
    // consecutive sequence.
    // Input:  [100, 4, 200, 1, 3, 2]
    // Output: 4   (sequence: 1, 2, 3, 4)
    //
    // Think about it: sorting works but costs O(n log n).
    // Can you do it in O(n) using a Set?
    // ─────────────────────────────────────────────────────────────
    static void optional2_LongestConsecutiveSequence() {
        System.out.println("\n=== Optional 2 – Longest Consecutive Sequence ===");

        List<Integer> numbers = Arrays.asList(100, 4, 200, 1, 3, 2);
        Set<Integer> set = new HashSet<>(numbers);

        int longest = 0;

        for (int num : set) {
            // Only start counting from a sequence's first element.
            // If (num - 1) exists in the set, num is not the start — skip it.
            if (!set.contains(num - 1)) {
                int current = num;
                int length = 1;

                while (set.contains(current + 1)) {
                    current++;
                    length++;
                }

                longest = Math.max(longest, length);
            }
        }

        System.out.println("Input:    " + numbers);
        System.out.println("Longest consecutive sequence: " + longest);
    }

    // ─────────────────────────────────────────────────────────────
    // Optional 3 – LRU Cache
    //
    // Implement a Least Recently Used (LRU) cache with capacity 3.
    // - put(key, value): add entry; evict the least recently used if full
    // - get(key): return value, or -1 if not found; counts as a recent use
    //
    // Think about it: which Java collection maintains insertion order
    // and gives you control over eviction?
    // ─────────────────────────────────────────────────────────────
    static void optional3_LruCache() {
        System.out.println("\n=== Optional 3 – LRU Cache ===");

        LRUCache cache = new LRUCache(3);

        cache.put(1, "A");
        System.out.println("put(1, A) → " + cache);

        cache.put(2, "B");
        System.out.println("put(2, B) → " + cache);

        cache.put(3, "C");
        System.out.println("put(3, C) → " + cache);

        // get(1) marks key 1 as recently used — it moves to the end
        System.out.println("get(1)    → " + cache.get(1) + ",  cache: " + cache);

        // Cache is full; adding key 4 evicts the least recently used (key 2)
        cache.put(4, "D");
        System.out.println("put(4, D) → " + cache + "  (key 2 evicted)");
    }

    /**
     * LinkedHashMap in access-order mode (accessOrder = true) moves an entry
     * to the end of the internal list every time it is accessed via get().
     * Overriding removeEldestEntry() tells it to evict the head (least recently
     * used) whenever size exceeds the capacity.
     */
    static class LRUCache extends LinkedHashMap<Integer, String> {
        private final int capacity;

        LRUCache(int capacity) {
            // accessOrder=true: iteration order reflects access order, not insertion order
            super(capacity, 0.75f, true);
            this.capacity = capacity;
        }

        public String get(int key) {
            // Returns null if not found — we convert that to -1 to match the spec
            String value = super.get(key);
            return value != null ? value : "-1";
        }

        public void put(int key, String value) {
            super.put(key, value);
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, String> eldest) {
            // Called after every put(); return true to evict the eldest entry
            return size() > capacity;
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Optional 4 – Graph as Adjacency List
    //
    // Represent an undirected graph using HashMap<String, List<String>>.
    // - Add at least 5 nodes and 6 edges
    // - Method: print all neighbours of a given node
    // - Method: check whether two nodes are directly connected
    // ─────────────────────────────────────────────────────────────
    static void optional4_GraphAdjacencyList() {
        System.out.println("\n=== Optional 4 – Graph as Adjacency List ===");

        Map<String, List<String>> graph = new HashMap<>();

        // Each edge is added in both directions because the graph is undirected
        addEdge(graph, "Alice", "Bob");
        addEdge(graph, "Alice", "Charlie");
        addEdge(graph, "Bob", "Diana");
        addEdge(graph, "Charlie", "Diana");
        addEdge(graph, "Diana", "Eve");
        addEdge(graph, "Bob", "Eve");

        printNeighbours(graph, "Alice");
        printNeighbours(graph, "Diana");

        System.out.println("\nAlice connected to Eve?     " + isConnected(graph, "Alice", "Eve"));
        System.out.println("Alice connected to Bob?     " + isConnected(graph, "Alice", "Bob"));
        System.out.println("Charlie connected to Eve?   " + isConnected(graph, "Charlie", "Eve"));
    }

    // Adds an undirected edge between two nodes
    static void addEdge(Map<String, List<String>> graph, String from, String to) {
        graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        graph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
    }

    // Prints all direct neighbours of the given node
    static void printNeighbours(Map<String, List<String>> graph, String node) {
        List<String> neighbours = graph.getOrDefault(node, Collections.emptyList());
        System.out.println("Neighbours of " + node + ": " + neighbours);
    }

    // Returns true if 'from' and 'to' share a direct edge
    static boolean isConnected(Map<String, List<String>> graph, String from, String to) {
        return graph.getOrDefault(from, Collections.emptyList()).contains(to);
    }

    // ─────────────────────────────────────────────────────────────
    // Optional 5 – Sublist with Target Sum
    //
    // Given a list of integers and a target sum, find ALL contiguous sublists
    // that add up to the target.
    // Input:  [1, 2, 3, 4, 2, 1, 5],  target = 6
    // Print the start index, end index, and the matching sublist for each match.
    // ─────────────────────────────────────────────────────────────
    static void optional5_SublistWithTargetSum() {
        System.out.println("\n=== Optional 5 – Sublist with Target Sum ===");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 2, 1, 5);
        int target = 6;
        boolean found = false;

        // Outer loop: starting index of the sublist
        for (int start = 0; start < numbers.size(); start++) {
            int sum = 0;
            // Inner loop: extend the sublist one element at a time
            for (int end = start; end < numbers.size(); end++) {
                sum += numbers.get(end);
                if (sum == target) {
                    // subList(start, end + 1) gives elements from start up to and including end
                    System.out.println("  Index " + start + "–" + end + ": " + numbers.subList(start, end + 1));
                    found = true;
                }
                // Early exit: if sum exceeds target, no need to extend further
                if (sum > target) break;
            }
        }

        if (!found) {
            System.out.println("No matching sublist found.");
        }
    }
}
