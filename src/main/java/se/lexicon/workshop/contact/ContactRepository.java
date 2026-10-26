package se.lexicon.workshop.contact;

import java.util.*;

/**
 * Stores and retrieves contacts in memory using an ArrayList.
 *
 * Why ArrayList?
 *   - Contacts are accessed by index, iterated in order, and searched by field value.
 *   - We don't need key-based O(1) lookup (that would favour a Map), and
 *     we do need ordering + duplicates-by-id (that rules out Set).
 *   - ArrayList gives us simple, readable CRUD that matches the workshop's scope.
 *
 * Constraint enforced here: no two contacts may share the same email address.
 */
public class ContactRepository {

    // The single source of truth for all contacts in this session
    private final ArrayList<Contact> contacts = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Bulk load (file storage only — skips validation)
    // -------------------------------------------------------------------------

    void loadAll(List<Contact> loaded) {
        contacts.addAll(loaded);
    }

    // -------------------------------------------------------------------------
    // Create
    // -------------------------------------------------------------------------

    /**
     * Adds a contact to the repository.
     *
     * @param contact the contact to add (must not be null)
     * @return the added contact
     * @throws IllegalArgumentException if a contact with the same email already exists
     */
    public Contact add(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact must not be null.");
        }
        // Unique-email constraint — checked before inserting
        if (findByEmail(contact.email).isPresent()) {
            throw new IllegalArgumentException(
                    "A contact with email '" + contact.email + "' already exists.");
        }
        contacts.add(contact);
        return contact;
    }

    // -------------------------------------------------------------------------
    // Read
    // -------------------------------------------------------------------------

    /**
     * Returns all contacts sorted alphabetically by name (Challenge 1 built-in).
     * The returned list is a defensive copy — callers cannot modify the store.
     */
    public List<Contact> findAll() {
        List<Contact> sorted = new ArrayList<>(contacts);
        sorted.sort(Comparator.comparing(c -> c.name.toLowerCase()));
        return Collections.unmodifiableList(sorted);
    }

    /**
     * Finds contacts whose name contains the given search term (case-insensitive).
     * Returns an empty list when nothing matches — never null.
     */
    public List<Contact> findByName(String namePart) {
        if (namePart == null || namePart.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String term = namePart.trim().toLowerCase();
        List<Contact> results = new ArrayList<>();
        for (Contact c : contacts) {
            if (c.name.toLowerCase().contains(term)) {
                results.add(c);
            }
        }
        return Collections.unmodifiableList(results);
    }

    /**
     * Finds a contact by exact email address (case-insensitive).
     * Returns Optional.empty() when no match is found.
     */
    public Optional<Contact> findByEmail(String email) {
        if (email == null) return Optional.empty();
        String target = email.trim().toLowerCase();
        for (Contact c : contacts) {
            if (c.email.toLowerCase().equals(target)) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    /**
     * Finds a contact by its unique id.
     * Returns Optional.empty() when no match is found.
     */
    public Optional<Contact> findById(int id) {
        for (Contact c : contacts) {
            if (c.id == id) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    // -------------------------------------------------------------------------
    // Update
    // -------------------------------------------------------------------------

    /**
     * Saves the current field values of an existing contact back into the store.
     * Matching is done by id — the id itself is never changed.
     *
     * @param updated a Contact whose id matches an existing entry
     * @return true if the contact was found and updated, false otherwise
     * @throws IllegalArgumentException if the new email is already used by a different contact
     */
    public boolean update(Contact updated) {
        if (updated == null) return false;

        // Prevent stealing another contact's email
        Optional<Contact> existingEmail = findByEmail(updated.email);
        if (existingEmail.isPresent() && existingEmail.get().id != updated.id) {
            throw new IllegalArgumentException(
                    "Email '" + updated.email + "' is already used by another contact.");
        }

        for (Contact c : contacts) {
            if (c.id == updated.id) {
                c.name  = updated.name;
                c.email = updated.email;
                c.phone = updated.phone;
                return true;
            }
        }
        return false; // id not found
    }

    // -------------------------------------------------------------------------
    // Delete
    // -------------------------------------------------------------------------

    /**
     * Removes the contact with the given id.
     *
     * @return true if a contact was removed, false if the id was not found
     */
    public boolean deleteById(int id) {
        return contacts.removeIf(c -> c.id == id);
    }

    // -------------------------------------------------------------------------
    // Utility
    // -------------------------------------------------------------------------

    /** Returns the number of contacts currently stored. */
    public int size() {
        return contacts.size();
    }
}
