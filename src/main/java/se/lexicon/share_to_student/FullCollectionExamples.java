package se.lexicon.share_to_student;

import java.util.*;

public class FullCollectionExamples {

    // Entry point – runs some examples.
    static void main() {
        ex1_ArrayList();
        ex2_LinkedList();
        ex3_HashSet();
        ex4_LinkedHashSet();
        ex5_TreeSet();
        ex6_EnumSet();
        ex7_ArrayDeque();
        ex8_PriorityQueue();
        ex9_HashMap();
        ex10_LinkedHashMap();
        ex11_TreeMap();
        ex12_EnumMap();
        ex13_WeakHashMap();
        ex14_IdentityHashMap();
        ex15_ArraysAsList();
        ex16_ObjectsUtility();
        ex17_CollectionsUtility();
    }

    // ---------------- EXAMPLES ---------------- //

    // EX1: ArrayList example
    // ArrayList is a resizable array implementation of List.
    // - Keeps elements in insertion order.
    // - Very fast random access (get by index).
    // - Fast adding to the end, slower inserting/removing in the middle.
    // Use Case: Lists of items where you often read by index
    // (e.g. team members, menu items, search results).
    static void ex1_ArrayList() {
        System.out.println("=== EX1: ArrayList ===");

        // 1. Create and add elements
        List<String> team = new ArrayList<>();
        team.add("Mehrdad");
        team.add("Simon");
        team.add("Elnaz");
        team.add("Marcus");

        System.out.println("Initial team: " + team);

        // 2. Add at a specific index
        // Insert Tobias at index 2 (before Elnaz)
        team.add(2, "Tobias");
        System.out.println("After adding Tobias at index 2: " + team);

        // 3. Access by index
        System.out.println("Second member (index 1): " + team.get(1));

        // 4. Remove by index
        String removedByIndex = team.remove(3); // remove element at index 3
        System.out.println("Removed by index (3): " + removedByIndex);
        System.out.println("After remove(index 3): " + team);

        // 5. Remove by value
        boolean removedByValue = team.remove("Simon");
        System.out.println("Was 'Simon' removed? " + removedByValue);
        System.out.println("After remove(\"Simon\"): " + team);

        // 6. Merge with another list (addAll)
        List<String> newMembers = new ArrayList<>();
        newMembers.add("Neda");
        newMembers.add("Arash");

        System.out.println("New members: " + newMembers);
        team.addAll(newMembers);
        System.out.println("After merging (addAll): " + team);

        // 7. Sort the list alphabetically
        Collections.sort(team);
        System.out.println("After Collections.sort (A–Z): " + team);

        // 8. Sort in reverse order (Z–A)
        team.sort(Collections.reverseOrder());
        System.out.println("After reverse sort (Z–A): " + team);

        System.out.println();
    }

    // EX2: LinkedList example
    // LinkedList is a doubly-linked list implementation of List and Deque.
    // - Fast insert/remove at the beginning and end.
    // - Slower random access (get by index is O(n)).
    // Use Case: Queues, task lists, histories where you often add/remove from the ends.
    static void ex2_LinkedList() {
        System.out.println("=== EX2: LinkedList ===");
        LinkedList<String> tasks = new LinkedList<>();

        tasks.addLast("Prepare slides");
        tasks.addLast("Fix server");
        tasks.addFirst("Deploy update");

        System.out.println("Tasks: " + tasks);
        System.out.println("First removed: " + tasks.removeFirst());
        System.out.println("Remaining tasks: " + tasks);
        System.out.println();
    }

    // EX3: HashSet example
    // HashSet is a Set implementation backed by a hash table.
    // - Stores unique elements only (no duplicates).
    // - Very fast add, remove, contains (on average O(1)).
    // - Does NOT guarantee any specific order.
    // Use Case: Unique usernames, unique IDs, membership checks.
    static void ex3_HashSet() {
        System.out.println("=== EX3: HashSet ===");
        Set<String> attendees = new HashSet<>();

        attendees.add("Tobias");
        attendees.add("Mehrdad");
        attendees.add("Tobias");  // duplicate ignored

        System.out.println("Attendees (no duplicates): " + attendees);
        System.out.println();
    }

    // EX4: LinkedHashSet example
    // LinkedHashSet is like HashSet but remembers insertion order.
    // - Unique elements.
    // - Iteration order is the order you inserted elements.
    // Use Case: Ordered unique collections, e.g. "recent items" lists.
    static void ex4_LinkedHashSet() {
        System.out.println("=== EX4: LinkedHashSet ===");
        Set<String> orderedSet = new LinkedHashSet<>();

        orderedSet.add("Marcus");
        orderedSet.add("Simon");
        orderedSet.add("Elnaz");

        System.out.println("Ordered unique set: " + orderedSet);
        System.out.println();
    }

    // EX5: TreeSet example
    // TreeSet is a SortedSet implementation using a red-black tree.
    // - Unique elements, kept sorted according to natural order or Comparator.
    // - Operations (add, remove, contains) are O(log n).
    // Use Case: Sorted unique names, tags, rankings.
    static void ex5_TreeSet() {
        System.out.println("=== EX5: TreeSet ===");
        Set<String> sortedNames = new TreeSet<>();

        sortedNames.add("Simon");
        sortedNames.add("Mehrdad");
        sortedNames.add("Elnaz");

        System.out.println("Sorted names (TreeSet): " + sortedNames);
        System.out.println();
    }

    // EX6: EnumSet example
    // EnumSet is a high-performance Set implementation for enum types.
    // - Internally uses bit flags, very fast and memory efficient.
    // Use Case: Representing flags/roles/states using enums.
    static void ex6_EnumSet() {
        System.out.println("=== EX6: EnumSet ===");

        enum Role {USER, ADMIN, MANAGER}

        EnumSet<Role> roles = EnumSet.of(Role.ADMIN, Role.USER);

        System.out.println("Roles (EnumSet): " + roles);
        System.out.println();
    }

    // EX7: ArrayDeque example (Stack + Queue)
    // ArrayDeque is a resizable-array implementation of Deque.
    // - Very fast for stack (LIFO) and queue (FIFO) operations.
    // - Preferred over Stack and LinkedList for these roles.
    // Use Case: Undo/redo stacks, task queues, history.
    static void ex7_ArrayDeque() {
        System.out.println("=== EX7: ArrayDeque ===");

        Deque<String> actions = new ArrayDeque<>();

        // Use as a stack (LIFO)
        actions.push("Open");
        actions.push("Edit");
        actions.push("Save");

        System.out.println("Stack pop: " + actions.pop());

        // Use as a queue (FIFO)
        actions.offer("Task1");
        actions.offer("Task2");

        System.out.println("Queue poll: " + actions.poll());
        System.out.println("Remaining deque: " + actions);
        System.out.println();
    }

    // EX8: PriorityQueue example
    // PriorityQueue is a heap-based implementation of a queue.
    // - By default it is a min-heap: smallest element comes out first.
    // Use Case: Task scheduling, always processing the "lowest" or "highest" priority element next.
    static void ex8_PriorityQueue() {
        System.out.println("=== EX8: PriorityQueue ===");

        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.offer(40);
        pq.offer(10);
        pq.offer(30);

        System.out.println("Queue contents (heap): " + pq);
        System.out.println("Lowest priority first (poll): " + pq.poll());
        System.out.println("After poll: " + pq);
        System.out.println();
    }

    // EX9: HashMap example
    // HashMap is a Map implementation backed by a hash table.
    // - Stores key–value pairs.
    // - Very fast get/put/remove (on average O(1)).
    // - Does NOT guarantee iteration order.
    // Use Case: Storing objects by ID, configuration maps, lookup tables.
    static void ex9_HashMap() {
        System.out.println("=== EX9: HashMap ===");
        Map<String, Integer> scores = new HashMap<>();

        scores.put("Mehrdad", 95);
        scores.put("Elnaz", 88);
        scores.put("Simon", 75);

        System.out.println("Scores: " + scores);
        System.out.println("Mehrdad score: " + scores.get("Mehrdad"));
        System.out.println();
    }

    // EX10: LinkedHashMap example
    // LinkedHashMap is a HashMap that maintains insertion order (or access order).
    // Use Case: Caches (like LRU), or any map where order matters.
    static void ex10_LinkedHashMap() {
        System.out.println("=== EX10: LinkedHashMap ===");
        Map<String, String> steps = new LinkedHashMap<>();

        steps.put("A", "Start");
        steps.put("B", "Continue");
        steps.put("C", "Finish");

        System.out.println("Steps: " + steps);
        System.out.println();
    }

    // EX11: TreeMap example
    // TreeMap is a Map implementation that keeps keys sorted.
    // - All keys are ordered according to natural order or a Comparator.
    // - Supports navigation methods (firstEntry, lastEntry, etc.).
    // Use Case: Sorted key–value data, ranking tables, time-series.
    static void ex11_TreeMap() {
        System.out.println("=== EX11: TreeMap ===");
        TreeMap<Integer, String> ranks = new TreeMap<>();

        ranks.put(3, "Simon");
        ranks.put(1, "Elnaz");
        ranks.put(2, "Marcus");

        System.out.println("Ranked map: " + ranks);
        System.out.println("Top rank (firstEntry): " + ranks.firstEntry());
        System.out.println();
    }

    // EX12: EnumMap example
    // EnumMap is a Map implementation optimized for enum keys.
    // - Very fast and space-efficient (keys are enum constants).
    // Use Case: Mapping states/roles/status enums to values or descriptions.
    static void ex12_EnumMap() {
        System.out.println("=== EX12: EnumMap ===");

        enum Status {NEW, IN_PROGRESS, DONE}

        EnumMap<Status, String> statusMap = new EnumMap<>(Status.class);

        statusMap.put(Status.NEW, "Waiting");
        statusMap.put(Status.IN_PROGRESS, "Working");
        statusMap.put(Status.DONE, "Completed");

        System.out.println("Status map: " + statusMap);
        System.out.println();
    }

    // EX13: WeakHashMap example
    // WeakHashMap uses weak references for keys.
    // - If a key is no longer strongly referenced elsewhere,
    //   its entry can be garbage-collected automatically.
    // Use Case: Caches that should not prevent objects from being GC'd.
    static void ex13_WeakHashMap() {
        System.out.println("=== EX13: WeakHashMap ===");

        Map<Object, String> cache = new WeakHashMap<>();
        Object key = new Object();
        cache.put(key, "Temporary Data");

        System.out.println("Before GC: " + cache);

        key = null;
        System.gc();

        System.out.println("After GC (entry may vanish): " + cache);
        System.out.println();
    }

    // EX14: IdentityHashMap example
    // IdentityHashMap compares keys by reference (==), not by equals().
    // - Two String objects with the same text but different references
    //   are treated as different keys.
    // Use Case: Very rare – when you really need identity semantics.
    static void ex14_IdentityHashMap() {
        System.out.println("=== EX14: IdentityHashMap ===");

        IdentityHashMap<String, Integer> map = new IdentityHashMap<>();
        map.put(new String("Test"), 1);
        map.put(new String("Test"), 2);  // Different reference, so different key

        System.out.println("IdentityMap: " + map);
        System.out.println();
    }

    // EX15: Arrays.asList example
    // Arrays.asList creates a fixed-size List backed by the given array.
    // - Changes to the array are reflected in the list and vice versa.
    // - You cannot add/remove elements from the resulting list.
    // Use Case: Quickly wrap an array as a List for read or iteration.
    static void ex15_ArraysAsList() {
        System.out.println("=== EX15: Arrays.asList ===");

        String[] arr = {"Mehrdad", "Marcus"};
        List<String> list = Arrays.asList(arr);

        System.out.println("List from array: " + list);
        System.out.println();
    }

    // EX16: Objects utility example
    // Objects is a utility class with helper methods for null-safety and equals/hash.
    // Use Case: Null checks, equals comparison, building hash codes.
    static void ex16_ObjectsUtility() {
        System.out.println("=== EX16: Objects Utility ===");

        String str = "Hello";
        Objects.requireNonNull(str, "String must not be null");

        boolean equal = Objects.equals("A", "A");
        int hash = Objects.hash("Mehrdad", 42);

        System.out.println("Equals(\"A\",\"A\"): " + equal);
        System.out.println("Hash for (\"Mehrdad\", 42): " + hash);
        System.out.println();
    }

    // EX17: Collections utility class example
    // The Collections class contains static helper methods for working with Collections.
    // Common operations:
    //  - Sorting (sort)
    //  - Shuffling (shuffle)
    //  - Reversing (reverse)
    //  - Finding max/min (max, min)
    //  - Counting frequency of elements (frequency)
    //  - Creating unmodifiable and synchronized views.
    static void ex17_CollectionsUtility() {
        System.out.println("=== EX17: Collections Utility ===");

        // 1. Create a list of numbers
        List<Integer> numbers = new ArrayList<>(Arrays.asList(5, 1, 3, 3, 9, 7));
        System.out.println("Original: " + numbers);

        // 2. Sort (natural order)
        Collections.sort(numbers);
        System.out.println("Sorted (ascending): " + numbers);

        // 3. Reverse the list
        Collections.reverse(numbers);
        System.out.println("Reversed: " + numbers);

        // 4. Shuffle the elements randomly
        Collections.shuffle(numbers);
        System.out.println("Shuffled: " + numbers);

        // 5. Find max and min
        int max = Collections.max(numbers);
        int min = Collections.min(numbers);
        System.out.println("Max: " + max + ", Min: " + min);

        // 6. Frequency of a value
        int freqOf3 = Collections.frequency(numbers, 3);
        System.out.println("Frequency of 3: " + freqOf3);

        // 7. Unmodifiable list (read-only view)
        List<Integer> unmodifiable = Collections.unmodifiableList(numbers);
        System.out.println("Unmodifiable view: " + unmodifiable);
        // unmodifiable.add(100); // would throw UnsupportedOperationException

        // 8. Synchronized list (thread-safe wrapper)
        List<Integer> syncList = Collections.synchronizedList(numbers);
        System.out.println("Synchronized list wrapper created (same content): " + syncList);

        System.out.println();
    }

}
