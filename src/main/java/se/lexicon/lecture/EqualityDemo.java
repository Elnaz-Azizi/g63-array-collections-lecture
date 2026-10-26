package se.lexicon.lecture;

import java.util.ArrayList;

public class EqualityDemo {


    void main() {
        equalityExample();

        ArrayList<Person> people = new ArrayList<>();

        Person p1 = new Person();
        p1.id = 1;
        p1.name = "Erik";

        people.add(p1);

        // Create a NEW object with the SAME data
        Person p2 = new Person();
        p2.id = 1;
        p2.name = "Erik";

        // Without overriding equals(), this would be FALSE
        boolean exists = people.contains(p2);
        IO.println("Does Erik exist in the list? " + exists);
    }

    public static void equalityExample() {
        // 1. Create two Person objects with identical data
        Person person1 = new Person();
        person1.id = 1;
        person1.name = "Mehrdad";
        person1.email = "mehrdad.javan@lexicon.se";

        Person person2 = new Person();
        person2.id = 1;
        person2.name = "Mehrdad";
        person2.email = "mehrdad.javan@lexicon.se";

        // 2. Compare using equals()
        // Every class in Java automatically inherits from the 'Object' class.
        // The default Object.equals() checks "Reference Equality" (memory address).
        boolean isEqualTwoPersons = person1.equals(person2);
        
        // Output will be FALSE. 
        // Why? Because person1 and person2 are two different objects at different memory locations.
        IO.println("isEqualTwoPersons (Default): " + isEqualTwoPersons);

        // 3. Compare with Strings
        String test1 = "TEST";
        String test2 = "TEST";

        boolean isEqualTwoStrings = test1.equals(test2);
        
        // Output will be TRUE. 
        // Why? The String class has ALREADY overridden the equals() method 
        // to compare the actual text (content) instead of the memory location.
        IO.println("isEqualTwoStrings: " + isEqualTwoStrings);
    }
}