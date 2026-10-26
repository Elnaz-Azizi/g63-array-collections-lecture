package se.lexicon.workshop.contact;

import java.util.Objects;

/**
 * Represents a single contact in the contact management system.
 *
 * Fields are accessed directly (e.g. contact.name, contact.email).
 * Each contact gets a unique id assigned automatically at construction.
 */
public class Contact {

    // Shared counter — increments every time a new Contact is created
    private static int nextId = 1;

    // These fields are public so they can be read and changed directly
    public int id;
    public String name;
    public String email;
    public String phone; // optional — may be empty

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Creates a new Contact with an automatically assigned id.
     *
     * @param name  the contact's full name
     * @param email the contact's email address
     * @param phone the contact's phone number (may be null or empty)
     */
    public Contact(String name, String email, String phone) {
        this.id    = nextId++;
        this.name  = name;
        this.email = email;
        this.phone = (phone != null) ? phone.trim() : "";
    }

    // Called after loading from file so new contacts get IDs above the loaded maximum
    static void resetCounter(int maxId) {
        nextId = maxId + 1;
    }

    // -------------------------------------------------------------------------
    // equals / hashCode — two contacts are the same if they have the same id
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return id == contact.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // -------------------------------------------------------------------------
    // toString — used when printing a contact to the console
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        String phoneDisplay = (phone == null || phone.isEmpty()) ? "(no phone)" : phone;
        return String.format("[%d] %-20s | %-30s | %s", id, name, email, phoneDisplay);
    }
}
