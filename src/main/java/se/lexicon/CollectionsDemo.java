package se.lexicon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class CollectionsDemo {

    void main() {
        // 1. Declaration and Initialization
        // Syntax: ArrayList<Type> listName = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();

        // 2. Adding Elements
        names.add("Erik");
        names.add("Fredrik");
        names.add("Jonas");
        names.add("Erik"); // Duplicates are allowed

        // 3. Accessing Elements (using index)
        IO.println("First name: " + names.get(0)); // Output: Erik

        // 4. Modifying Elements
        names.set(2, "Marcus"); // Replaces "Jonas" with "Marcus"

        // 5. Removing Elements
        names.remove("Erik"); // Removes the first occurrence of "Erik"
        names.remove(1);      // Removes element at index 1

        // 6. Size of the ArrayList
        IO.println("Size: " + names.size());

        // 7. Checking if an element exists
        boolean containsFredrik = names.contains("Fredrik");

        // 8. Sorting
        Collections.sort(names);

        // 9. Iterating
        for (String name : names) {
            IO.println(name);
        }

        collectionsExamples();
        objectsExamples();
    }

    public static void collectionsExamples() {
        ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(10, 20, 30, 40, 50));

        // 1. Reversing
        Collections.reverse(numbers);
        IO.println("Reversed: " + numbers);

        // 2. Shuffling
        Collections.shuffle(numbers);
        IO.println("Shuffled: " + numbers);

        // 3. Finding Min and Max
        IO.println("Min: " + Collections.min(numbers));
        IO.println("Max: " + Collections.max(numbers));

        // 4. Sorting
        Collections.sort(numbers);
        IO.println("Sorted: " + numbers);

        // 5. Binary Search (Requires sorted list)
        int index = Collections.binarySearch(numbers, 30);
        IO.println("Index of 30: " + index);
    }

    public static void objectsExamples() {
        String name1 = "Erik";
        String name2 = null;

        // 1. Null-safe equality check
        IO.println("Equals (Erik, null): " + Objects.equals(name1, name2));

        // 2. Null-safe toString
        IO.println("ToString: " + Objects.toString(name2, "Default Name"));

        // 3. Check for null
        IO.println("IsNull: " + Objects.isNull(name2));

        // 4. Require non-null (Throws exception if null)
        try {
            Objects.requireNonNull(name2, "Name cannot be null!");
        } catch (NullPointerException e) {
            IO.println("Exception: " + e.getMessage());
        }
    }
}