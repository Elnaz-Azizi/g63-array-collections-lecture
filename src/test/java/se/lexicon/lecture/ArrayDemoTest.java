package se.lexicon.lecture;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ArrayDemoTest {

    @Test
    @DisplayName("Find max in a normal positive array (Happy Path)")
    void findMaxNumberWithPositiveNumbers() {
        // Arrange
        int[] numbers = {12, 5, 45, 18, 33, 24};
        int expected = 45;

        // Act
        int actual = ArrayDemo.findMaxNumber(numbers);

        // Assert (Verify)
        assertEquals(expected, actual, "The maximum should be 45");
    }

    @Test
    @DisplayName("Find max in an array with negative numbers")
    void findMaxNumberWithNegativeNumbers() {
        // Arrange
        int[] numbers = {-12, -5, -45, -18, -33, -24};
        int expected = -5;

        // Act
        int actual = ArrayDemo.findMaxNumber(numbers);

        // Assert (Verify)
        assertEquals(expected, actual, "The maximum should be -5");
    }

    @Test
    @DisplayName("Test findMaxNumber with null array (Negative Case)")
    void findMaxNumberWithNullArray() {
        // Arrange
        int[] numbers = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> ArrayDemo.findMaxNumber(numbers), "Should throw IllegalArgumentException for null array");
    }

}
