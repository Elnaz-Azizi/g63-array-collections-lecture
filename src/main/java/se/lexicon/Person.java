package se.lexicon;

import java.util.Objects;

public class Person {
    int id;
    String name;
    String email;


    @Override
    public boolean equals(Object o) {
        // 1. Check if comparing to the exact same instance
        if (this == o) return true;

        // 2. Check if the other object is null or a different class
        if (o == null || getClass() != o.getClass()) return false;

        // 3. Cast to Person and compare the fields
        Person person = (Person) o;
        return id == person.id &&
                Objects.equals(name, person.name) &&
                Objects.equals(email, person.email);
    }

    @Override
    public int hashCode() {
        // Generates a hash based on the same fields used in equals()
        return Objects.hash(id, name, email);
    }
}
