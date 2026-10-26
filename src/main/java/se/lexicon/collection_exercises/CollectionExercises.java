package se.lexicon.collection_exercises;

import java.util.*;

/**
 * Solutions for Collection Exercises 1–17.
 * Each method starts with the exercise question as a comment,
 * followed by a clean implementation with explanations.
 * <p>
 * Run main() to execute all non-interactive exercises.
 * Exercises 3 and 5 require user input — run them separately.
 */
public class CollectionExercises {

    void main() {
        exercise01_ShoppingList();
        exercise02_UniqueCities();
        // exercise03_PhoneBook();       // requires user input — run separately
        exercise04_FindLargest();
        // exercise05_CountByLetter();   // requires user input — run separately
        exercise06_WordFrequency();
        exercise07_RemoveDuplicatesKeepOrder();
        exercise08_MergeWithoutDuplicates();
        exercise09_SubmissionTracker();
        exercise10_ConcertQueue();
        exercise11_StudentGrades();
        exercise12_GroupByFirstLetter();
        exercise13_MostFrequentWord();
        exercise14_CommonElements();
        exercise15_TwoSum();
        exercise16_SecondLargest();
        exercise17_Top3MostFrequent();
    }

    // ─────────────────────────────────────────────────────────────
    // Exercise 1 – Shopping List
    //
    // You are managing a shopping list.
    // - Create an ArrayList<String> and add 5 items
    // - Print all items numbered (1, 2, 3…)
    // - Remove one item by name
    // - Print the updated list
    // ─────────────────────────────────────────────────────────────
    static void exercise01_ShoppingList() {
        IO.println("\n=== Exercise 1 – Shopping List ===");

        List<String> shoppingList = new ArrayList<>();
        shoppingList.add("Milk");
        shoppingList.add("Bread");
        shoppingList.add("Eggs");
        shoppingList.add("Butter");
        shoppingList.add("Coffee");

        IO.println("Shopping list:");
        for (int i = 0; i < shoppingList.size(); i++) {
            IO.println("  " + (i + 1) + ". " + shoppingList.get(i));
        }

        // remove(Object) removes by value, not by index
        String removed = "Eggs";
        shoppingList.remove(removed);

        IO.println("\nAfter removing '" + removed + "':");
        for (int i = 0; i < shoppingList.size(); i++) {
            IO.println("  " + (i + 1) + ". " + shoppingList.get(i));
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Exercise 2 – Unique Cities
    //
    // Given: Stockholm, Gothenburg, Malmö, Stockholm, Uppsala, Gothenburg, Lund
    // - Store them in a collection that automatically handles duplicates
    // - Print all unique cities and the total count
    // ─────────────────────────────────────────────────────────────
    static void exercise02_UniqueCities() {
        IO.println("\n=== Exercise 2 – Unique Cities ===");

        List<String> cities = Arrays.asList(
                "Stockholm", "Gothenburg", "Malmö", "Stockholm",
                "Uppsala", "Gothenburg", "Lund"
        );

        // HashSet automatically rejects duplicates — no manual checking needed
        Set<String> uniqueCities = new HashSet<>(cities);

        IO.println("Unique cities: " + uniqueCities);
        IO.println("Total unique: " + uniqueCities.size());
    }

    // ─────────────────────────────────────────────────────────────
    // Exercise 3 – Phone Book
    //
    // Build a simple phone book.
    // - Store at least 4 contacts as name → phone number pairs
    // - Ask the user to enter a name and look it up
    // - Print the phone number if found, or "Contact not found." if not
    // ─────────────────────────────────────────────────────────────
    static void exercise03_PhoneBook() {
        IO.println("\n=== Exercise 3 – Phone Book ===");

        Map<String, String> phoneBook = new HashMap<>();
        phoneBook.put("Mehrdad Javan", "0712345678");
        phoneBook.put("Elnaz Azizi", "0798765432");
        phoneBook.put("Simon Elbrink", "0789456123");
        phoneBook.put("Anna Karlsson", "0701122334");

        String name = IO.readln("Enter a name to look up: ").trim();

        // containsKey() avoids a NullPointerException on missing keys
        if (phoneBook.containsKey(name)) {
            IO.println("Phone number: " + phoneBook.get(name));
        } else {
            IO.println("Contact not found.");
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Exercise 4 – Find the Largest
    //
    // Given: 14, 3, 78, 45, 22, 91, 7, 56
    // - Find and print the largest number
    // - Do not use Collections.max() — use a loop
    // ─────────────────────────────────────────────────────────────
    static void exercise04_FindLargest() {
        IO.println("\n=== Exercise 4 – Find the Largest ===");

        List<Integer> numbers = Arrays.asList(14, 3, 78, 45, 22, 91, 7, 56);

        // Seed with the first element — never assume 0 is a safe starting value
        int largest = numbers.get(0);
        for (int number : numbers) {
            if (number > largest) {
                largest = number;
            }
        }

        IO.println("List:    " + numbers);
        IO.println("Largest: " + largest);
    }

    // ─────────────────────────────────────────────────────────────
    // Exercise 5 – Count by Letter
    //
    // Given: Anna, Bob, Alice, Charlie, Amanda, David, Amy, Brian
    // - Ask the user to enter a letter
    // - Count and print how many names start with that letter (case-insensitive)
    // ─────────────────────────────────────────────────────────────
    static void exercise05_CountByLetter() {
        IO.println("\n=== Exercise 5 – Count by Letter ===");

        List<String> names = Arrays.asList("Anna", "Bob", "Alice", "Charlie", "Amanda", "David", "Amy", "Brian");

        char letter = Character.toUpperCase(IO.readln("Enter a letter: ").trim().charAt(0));

        int count = 0;
        for (String name : names) {
            if (Character.toUpperCase(name.charAt(0)) == letter) {
                count++;
            }
        }

        IO.println("Names starting with '" + letter + "': " + count);
    }

    // ─────────────────────────────────────────────────────────────
    // Exercise 6 – Word Frequency
    //
    // Given: apple, banana, apple, orange, banana, apple, grape, orange
    // - Count how many times each word appears
    // - Print each word and its count, sorted alphabetically
    // ─────────────────────────────────────────────────────────────
    static void exercise06_WordFrequency() {
        IO.println("\n=== Exercise 6 – Word Frequency ===");

        List<String> words = Arrays.asList(
                "apple", "banana", "apple", "orange", "banana", "apple", "grape", "orange"
        );

        // HashMap accumulates counts — getOrDefault handles the first occurrence
        Map<String, Integer> frequency = new HashMap<>();
        for (String word : words) {
            frequency.put(word, frequency.getOrDefault(word, 0) + 1);
        }

        // Wrapping in TreeMap sorts entries alphabetically by key
        new TreeMap<>(frequency).forEach((word, count) ->
                IO.println("  " + word + ": " + count)
        );
    }

    // ─────────────────────────────────────────────────────────────
    // Exercise 7 – Remove Duplicates, Keep Order
    //
    // Given: 4, 7, 2, 7, 9, 4, 1, 9, 3
    // - Produce a new list with duplicates removed, preserving original order
    //
    // Think about it: which collection helps track what you have already seen?
    // ─────────────────────────────────────────────────────────────
    static void exercise07_RemoveDuplicatesKeepOrder() {
        IO.println("\n=== Exercise 7 – Remove Duplicates, Keep Order ===");

        List<Integer> numbers = Arrays.asList(4, 7, 2, 7, 9, 4, 1, 9, 3);

        // HashSet tracks seen values in O(1) — add() returns false if already present
        Set<Integer> seen = new HashSet<>();
        List<Integer> result = new ArrayList<>();

        for (int number : numbers) {
            if (seen.add(number)) {
                result.add(number);
            }
        }

        IO.println("Original: " + numbers);
        IO.println("Result:   " + result);
    }

    // ─────────────────────────────────────────────────────────────
    // Exercise 8 – Merge Without Duplicates
    //
    // List A: 1, 2, 3, 4, 5
    // List B: 3, 4, 5, 6, 7
    // - Merge into a single collection with no duplicates
    // - Print the result and the total count
    // ─────────────────────────────────────────────────────────────
    static void exercise08_MergeWithoutDuplicates() {
        IO.println("\n=== Exercise 8 – Merge Without Duplicates ===");

        List<Integer> listA = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> listB = Arrays.asList(3, 4, 5, 6, 7);

        // addAll() on a HashSet silently drops any value already present
        Set<Integer> merged = new HashSet<>();
        merged.addAll(listA);
        merged.addAll(listB);

        IO.println("List A:  " + listA);
        IO.println("List B:  " + listB);
        IO.println("Merged:  " + merged);
        IO.println("Count:   " + merged.size());
    }

    // ─────────────────────────────────────────────────────────────
    // Exercise 9 – Choose Your Collection: Submission Tracker
    //
    // You are building a homework submission tracker.
    // - A student can only submit once — duplicates must be rejected with a message
    // - You only need to know WHETHER a student has submitted, not what they submitted
    // - The order submissions arrived in does not matter
    //
    // Collection chosen: HashSet<String>
    // Why: membership check is the only operation needed — HashSet gives O(1) add()
    //      and automatically rejects duplicates. No ordering is required.
    // ─────────────────────────────────────────────────────────────
    static void exercise09_SubmissionTracker() {
        IO.println("\n=== Exercise 9 – Submission Tracker ===");

        Set<String> submitted = new HashSet<>();

        List<String> attempts = Arrays.asList(
                "Alice", "Bob", "Charlie", "Alice", "Diana",
                "Bob", "Eve", "Frank", "Alice", "George"
        );

        for (String student : attempts) {
            // add() returns false if the value was already in the set
            if (!submitted.add(student)) {
                IO.println("  Rejected: " + student + " has already submitted.");
            }
        }

        IO.println("\nAll submitted students (" + submitted.size() + "): " + submitted);
    }

    // ─────────────────────────────────────────────────────────────
    // Exercise 10 – Choose Your Collection: Concert Queue
    //
    // You are managing a waiting queue outside a concert venue.
    // - People join the back and are let in from the front (FIFO)
    // - You need to add to the back and remove from the front efficiently
    //
    // Collection chosen: LinkedList used via the Queue interface
    // Why: Queue enforces FIFO. LinkedList gives O(1) offer() (add to back)
    //      and O(1) poll() (remove from front). ArrayDeque is also a valid choice.
    // ─────────────────────────────────────────────────────────────
    static void exercise10_ConcertQueue() {
        IO.println("\n=== Exercise 10 – Concert Queue ===");

        Queue<String> queue = new LinkedList<>();
        queue.offer("Mehrdad");
        queue.offer("Elnaz");
        queue.offer("Simon");
        queue.offer("Anna");
        queue.offer("David");
        queue.offer("Sara");

        IO.println("Queue:         " + queue);

        // poll() removes and returns the head (front) of the queue
        IO.println("\nLetting in: " + queue.poll());
        IO.println("Letting in: " + queue.poll());

        IO.println("\nRemaining queue: " + queue);
    }

    // ─────────────────────────────────────────────────────────────
    // Exercise 11 – Choose Your Collection: Student Grades
    //
    // You need to store and look up student grades by name.
    // - Each student has exactly one grade (0–100)
    // - Look up a student's grade quickly by name
    // - Print all students sorted by grade from highest to lowest
    //
    // Collection chosen: HashMap<String, Integer> for storage
    // Why: name-to-grade lookup is O(1). HashMap itself has no ordering,
    //      so for the sorted view we extract entries into a List and sort.
    // ─────────────────────────────────────────────────────────────
    static void exercise11_StudentGrades() {
        IO.println("\n=== Exercise 11 – Student Grades ===");

        Map<String, Integer> grades = new HashMap<>();
        grades.put("Alice", 92);
        grades.put("Bob", 74);
        grades.put("Charlie", 88);
        grades.put("Diana", 95);
        grades.put("Eve", 61);
        grades.put("Frank", 79);

        // Direct lookup by name — O(1)
        IO.println("Charlie's grade: " + grades.get("Charlie"));

        // Copy entries into a List so we can sort them
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(grades.entrySet());
        entries.sort((a, b) -> b.getValue() - a.getValue()); // descending by value

        IO.println("\nAll students sorted by grade:");
        for (Map.Entry<String, Integer> entry : entries) {
            IO.println("  " + entry.getKey() + ": " + entry.getValue());
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Exercise 12 – Group by First Letter
    //
    // Given: Alice, Bob, Anna, Charlie, Brian, Amanda, Carl
    // - Group the names by their first letter
    // - Print each group
    // ─────────────────────────────────────────────────────────────
    static void exercise12_GroupByFirstLetter() {
        IO.println("\n=== Exercise 12 – Group by First Letter ===");

        List<String> names = Arrays.asList("Alice", "Bob", "Anna", "Charlie", "Brian", "Amanda", "Carl");

        Map<Character, List<String>> groups = new HashMap<>();

        for (String name : names) {
            char key = name.charAt(0);
            // computeIfAbsent creates a new list for unseen keys automatically
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(name);
        }

        // TreeMap sorts groups alphabetically by key
        new TreeMap<>(groups).forEach((letter, nameList) ->
                IO.println("  " + letter + ": " + nameList)
        );
    }

    // ─────────────────────────────────────────────────────────────
    // Exercise 13 – Most Frequent Word
    //
    // Given: cat, dog, cat, bird, dog, cat, fish, dog, dog
    // - Find the word that appears most often
    // - Print it and its count
    // ─────────────────────────────────────────────────────────────
    static void exercise13_MostFrequentWord() {
        IO.println("\n=== Exercise 13 – Most Frequent Word ===");

        List<String> words = Arrays.asList("cat", "dog", "cat", "bird", "dog", "cat", "fish", "dog", "dog");

        Map<String, Integer> frequency = new HashMap<>();
        for (String word : words) {
            frequency.put(word, frequency.getOrDefault(word, 0) + 1);
        }

        // Single pass over the map to find the entry with the highest count
        String mostFrequent = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : frequency.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequent = entry.getKey();
            }
        }

        IO.println("Most frequent: " + mostFrequent + " (" + maxCount + ")");
    }

    // ─────────────────────────────────────────────────────────────
    // Exercise 14 – Common Elements
    //
    // List A: 1, 3, 5, 7, 9, 11
    // List B: 3, 6, 9, 12, 15
    // - Find all elements that appear in both lists
    // - Each element should appear only once in the result
    // ─────────────────────────────────────────────────────────────
    static void exercise14_CommonElements() {
        IO.println("\n=== Exercise 14 – Common Elements ===");

        List<Integer> listA = Arrays.asList(1, 3, 5, 7, 9, 11);
        List<Integer> listB = Arrays.asList(3, 6, 9, 12, 15);

        // Load listA into a set for O(1) lookup instead of O(n) per check
        Set<Integer> setA = new HashSet<>(listA);
        Set<Integer> common = new HashSet<>();

        for (int number : listB) {
            if (setA.contains(number)) {
                common.add(number);
            }
        }

        IO.println("List A:  " + listA);
        IO.println("List B:  " + listB);
        IO.println("Common:  " + common);
    }

    // ─────────────────────────────────────────────────────────────
    // Exercise 15 – Two Sum
    //
    // Numbers: 2, 7, 11, 15, 4, 6   Target: 13
    // - Find two numbers that add up to the target and print them
    //
    // Think about it: can you avoid a nested loop?
    // For each number, its complement is (target - number).
    // A HashMap lets you check if the complement was seen before in O(1).
    // ─────────────────────────────────────────────────────────────
    static void exercise15_TwoSum() {
        IO.println("\n=== Exercise 15 – Two Sum ===");

        List<Integer> numbers = Arrays.asList(2, 7, 11, 15, 4, 6);
        int target = 13;

        // Key = number seen so far, Value = its index (to avoid reusing the same element)
        Map<Integer, Integer> seen = new HashMap<>();

        for (int i = 0; i < numbers.size(); i++) {
            int current = numbers.get(i);
            int complement = target - current;

            if (seen.containsKey(complement)) {
                IO.println("Found: " + complement + " + " + current + " = " + target);
                return;
            }
            seen.put(current, i);
        }

        IO.println("No pair found.");
    }

    // ─────────────────────────────────────────────────────────────
    // Exercise 16 – Second Largest
    //
    // Given: 34, 78, 23, 78, 91, 56, 91, 12
    // - Find the second largest UNIQUE value
    // - Do not sort the list
    // ─────────────────────────────────────────────────────────────
    static void exercise16_SecondLargest() {
        IO.println("\n=== Exercise 16 – Second Largest ===");

        List<Integer> numbers = Arrays.asList(34, 78, 23, 78, 91, 56, 91, 12);

        // Track the two highest unique values in a single pass
        int first = Integer.MIN_VALUE;
        int second = Integer.MIN_VALUE;

        for (int number : numbers) {
            if (number > first) {
                second = first; // old first becomes second
                first = number;
            } else if (number > second && number != first) {
                // Only update second if the number is a different value than first
                second = number;
            }
        }

        IO.println("List:            " + numbers);
        IO.println("Second largest:  " + second);
    }

    // ─────────────────────────────────────────────────────────────
    // Exercise 17 – Top 3 Most Frequent
    //
    // Given: java, python, java, c++, python, java, ruby, c++, python, ruby, java
    // - Find the 3 most frequently occurring words
    // - Print them in order from most to least frequent
    // ─────────────────────────────────────────────────────────────
    static void exercise17_Top3MostFrequent() {
        IO.println("\n=== Exercise 17 – Top 3 Most Frequent ===");

        List<String> words = Arrays.asList(
                "java", "python", "java", "c++", "python", "java",
                "ruby", "c++", "python", "ruby", "java"
        );

        Map<String, Integer> frequency = new HashMap<>();
        for (String word : words) {
            frequency.put(word, frequency.getOrDefault(word, 0) + 1);
        }

        // Sort all entries by count descending, then take the first 3
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(frequency.entrySet());
        entries.sort((a, b) -> b.getValue() - a.getValue());

        IO.println("Top 3 most frequent:");
        for (int i = 0; i < Math.min(3, entries.size()); i++) {
            Map.Entry<String, Integer> entry = entries.get(i);
            IO.println("  " + (i + 1) + ". " + entry.getKey() + " (" + entry.getValue() + ")");
        }
    }
}
