package se.lexicon;

import java.util.Arrays;

public class ArrayDemo {
     void main() {

        // 1. Declaration
        int[] numbers;  // Declares an array of integers
        String[] names; // Declares an array of Strings

        // 2. Initialization with Size
        // An array is a **fixed-size** data structure and cannot be resized once initialized.
        numbers = new int[3]; // Creates an array of size 3 with default values [0, 0, 0]

        //   Visual representation of 'numbers' array:
        //   | Index | 0 | 1 | 2 |
        //   |-------|---|---|---|
        //   | Value | 0 | 0 | 0 |

        // 3. Combined Declaration and Initialization
        String[] stringArray = new String[3]; // [null, null, null]

        // 4. Initializing with Specific Values
        int[] numbersArr = {2, 4, 6, 8}; // Shortened syntax (initializes and assigns values)

        // 5. Accessing and Modifying Elements
        String[] students = {"Fredrik", "Erik", "Jonas"};

        //    Visual representation of 'students' array:
        //    | Index |     0     |    1   |    2    |
        //    |-------|-----------|--------|---------|
        //    | Value | "Fredrik" | "Erik" | "Jonas" |

        IO.println(students[0]); // Output: Fredrik
        IO.println(students[2]); // Output: Jonas

        // [WARNING]
        // Accessing an index outside the range 0 to length - 1 throws an ArrayIndexOutOfBoundsException.
        // IO.println(students[3]); // This would throw an exception

        // Two-Dimensional Array (2D Array)
        // A 2D array can be thought of as an array of arrays, or a table with rows and columns.
        int[][] matrix = {
                {1, 2, 3}, // Row 0
                {4, 5, 6}, // Row 1
                {7, 8, 9}  // Row 2
        };

        /*
           Visual representation of 'matrix' (2D array):
           Column:   0   1   2
           Row 0:  | 1 | 2 | 3 |
           Row 1:  | 4 | 5 | 6 |
           Row 2:  | 7 | 8 | 9 |
        */

        // Accessing elements in a 2D array:
        // Use matrix[rowIndex][columnIndex]
        int element = matrix[1][2]; // Accesses Row 1, Column 2 -> 6
        IO.println("Element at [1][2]: " + element);

        // Modifying elements:
        matrix[0][0] = 10; // Changes Row 0, Column 0 from 1 to 10

         arrayIterationExample();
        expandingArray();
        expandingArrayUsingCopyOf();
        IO.println("Max Number: " + findMaxNumber(new int[]{1, 2, 3, 4, 5}));

    }

    public static void arrayIterationExample() {
        int[] numbers = {5, 2, 3, 4, 7};

        IO.println("Iterating using index-based for loop:");
        for (int i = 0; i < numbers.length; i++) {
            if (i == 2) {
                numbers[i] = 200; // Modifying the element at index 2
            }
        }
        // Arrays is a utility class in Java that provides methods for working with arrays.
        // .toString() converts the array into a readable String format: [5, 2, 200, 4, 7]
        IO.println(Arrays.toString(numbers));

        IO.println("Iterating using enhanced for loop:");
        for (int number : numbers) {
            // Modifying the loop variable does NOT change the actual array element.
            if (number == 5) {
                number = 5000; // Only changes the local 'number' variable (a copy)
            }
        }
        IO.println(Arrays.toString(numbers));
    }


    /**
     * Manual array expansion.
     * Since arrays have a fixed size, we must:
     * 1. Create a new, larger array.
     * 2. Copy elements from the old array to the new one.
     * 3. Assign the new element.
     */
    public static void expandingArray() {
        int[] originalArray = {2, 5, 8, 1, 9};
        int newElement = 40;

        // Step 1: Create a new array with size + 1
        int[] expandedArray = new int[originalArray.length + 1];
        /*
           Visual representation of 'expandedArray' (initially):
           | Index | 0 | 1 | 2 | 3 | 4 | 5 |
           |-------|---|---|---|---|---|---|
           | Value | 0 | 0 | 0 | 0 | 0 | 0 |
        */

        // Step 2: Copy elements manually using a for-loop
        for (int i = 0; i < originalArray.length; i++) {
            expandedArray[i] = originalArray[i];
        }

        /*
           Visual representation of 'expandedArray' (after Step 2):
           | Index | 0 | 1 | 2 | 3 | 4 | 5 |
           |-------|---|---|---|---|---|---|
           | Value | 2 | 5 | 8 | 1 | 9 | 0 |
        */

        // Step 3: Add the new element to the last position
        expandedArray[expandedArray.length - 1] = newElement;

        /*
           Visual representation of 'expandedArray' (after Step 3):
           | Index | 0 | 1 | 2 | 3 | 4 |  5  |
           |-------|---|---|---|---|---|-----|
           | Value | 2 | 5 | 8 | 1 | 9 | 40  |
        */

        IO.println("Expanded: " + Arrays.toString(expandedArray));
    }

    /**
     * Demonstrates array expansion using the built-in Arrays.copyOf() method.
     * This utility handles the creation of the new array and copying of elements in one step.
     */
    public static void expandingArrayUsingCopyOf() {
        int[] originalArray = {2, 5, 8, 1, 9};
        int newElement = 40;

        // Arrays.copyOf() creates a new array and copies existing elements
        int[] expandedArray = Arrays.copyOf(originalArray, originalArray.length + 1);

        /*
           Visual representation of 'expandedArray' (after Arrays.copyOf):
           | Index | 0 | 1 | 2 | 3 | 4 | 5 |
           |-------|---|---|---|---|---|---|
           | Value | 2 | 5 | 8 | 1 | 9 | 0 |
        */

        // Add the new element to the last position
        expandedArray[expandedArray.length - 1] = newElement;

        /*
           Visual representation of 'expandedArray' (final state):
           | Index | 0 | 1 | 2 | 3 | 4 |  5  |
           |-------|---|---|---|---|---|-----|
           | Value | 2 | 5 | 8 | 1 | 9 | 40  |
        */

        IO.println("Expanded (Arrays.copyOf): " + Arrays.toString(expandedArray));
    }


    /*
     * Question:
     * Write a program that finds the maximum number in a given array of integers.
     */
    // You should iterate through the array and return the highest value.
    public static int findMaxNumber(int[] numbers) {
    /*
        START

        SET array to the list of integers
        SET max to the first element of array

        FOR each element value in array starting from the second element
            IF value > max
                SET max to value
            END IF
        END FOR

        PRINT max

        END

     */
        /*
        int[] numbers = {12, 5, 45, 18, 33, 24};
         * Step 1: Check if the array is empty.
         * - If the array is empty, throw an exception to handle the invalid input.
         * - This ensures the program doesn't proceed with an empty array, which would result in an error.
         */
        if (numbers == null || numbers.length == 0) {
          throw new IllegalArgumentException("Array is empty");
        }

        /*
         * Step 2: Initialize a variable to store the maximum value found.
         * - Set `maxNumber` to the first element of the array (numbers[0]).
         * - This is necessary because we haven't encountered any elements yet,
         *   so the first element serves as the initial "maximum."
         */
        int maxNumber = numbers[0];

        /*
         * Step 3: Iterate through the array to find the maximum value.
         * - Start the loop from the second element (index 1) since the first element is already stored in `maxNumber`.
         * - Compare each element with the current value of `maxNumber`.
         * - If the current element is greater than `maxNumber`, update `maxNumber`.
         */
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] > maxNumber) {
                maxNumber = numbers[i]; // Update `maxNumber` with the new maximum value
            }
        }

        /*
         * Step 4: The maximum value is stored in `maxNumber`.
         * - The loop ensures that all elements are checked, and the highest value is identified.
         */
        IO.println("Maximum Number: " + maxNumber);

        return maxNumber;
    }
}
