package se.lexicon.workshop.contact;

import java.util.List;
import java.util.Optional;

/**
 * Entry point for the Contact Management App.
 *
 * Architecture (Separation of Concerns):
 *   ContactApp        — user interface: reads input, prints output, drives the menu loop
 *   ContactRepository — data layer: stores and retrieves contacts
 *   Contact           — model: holds the data for one contact
 *
 * Each menu option is handled by its own private static method (SRP).
 * No business logic lives in this class — it only delegates to the repository.
 */
public class ContactApp {

    // -------------------------------------------------------------------------
    // Entry point
    // -------------------------------------------------------------------------

    void main() {
        ContactRepository repository = new ContactRepository();

        // Load saved contacts on startup
        var saved = ContactFileStorage.load();
        if (!saved.isEmpty()) {
            repository.loadAll(saved);
            IO.println("Loaded " + saved.size() + " contact(s) from " + ContactFileStorage.FILE_PATH + ".");
        }

        IO.println("=== Contact Management App ===");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1  -> addContact(repository);
                case 2  -> listAllContacts(repository);
                case 3  -> searchByName(repository);
                case 4  -> searchByEmail(repository);
                case 5  -> updateContact(repository);
                case 6  -> deleteContact(repository);
                case 0  -> running = false;
                default -> IO.println("  Invalid option. Please choose 0-6.");
            }
        }

        // Save all contacts to file before exiting
        ContactFileStorage.save(repository.findAll());
        IO.println("Contacts saved to " + ContactFileStorage.FILE_PATH + ".");
        IO.println("Goodbye!");
    }

    // -------------------------------------------------------------------------
    // Menu display
    // -------------------------------------------------------------------------

    private static void printMenu() {
        IO.println("");
        IO.println("----------------------------------------");
        IO.println("  1. Add contact");
        IO.println("  2. List all contacts");
        IO.println("  3. Search by name");
        IO.println("  4. Search by email");
        IO.println("  5. Update contact");
        IO.println("  6. Delete contact");
        IO.println("  0. Exit");
        IO.println("----------------------------------------");
    }

    // -------------------------------------------------------------------------
    // Option 1 – Add a new contact
    // -------------------------------------------------------------------------

    private static void addContact(ContactRepository repository) {
        IO.println("\n-- Add Contact --");
        String name  = readString("  Name  : ");
        String email = readString("  Email : ");
        String phone = readString("  Phone (press Enter to skip): ");

        try {
            Contact added = repository.add(new Contact(name, email, phone));
            IO.println("  Saved: " + added);
        } catch (IllegalArgumentException e) {
            // Duplicate email — show the problem, don't crash
            IO.println("  Error: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Option 2 – List all contacts (sorted alphabetically — Challenge 1)
    // -------------------------------------------------------------------------

    private static void listAllContacts(ContactRepository repository) {
        IO.println("\n-- All Contacts (sorted A-Z) --");
        List<Contact> all = repository.findAll();

        if (all.isEmpty()) {
            IO.println("  No contacts found.");
            return;
        }

        printHeader();
        for (Contact c : all) {
            IO.println("  " + c);
        }
        IO.println("  Total: " + all.size() + " contact(s).");
    }

    // -------------------------------------------------------------------------
    // Option 3 – Search by name (partial, case-insensitive)
    // -------------------------------------------------------------------------

    private static void searchByName(ContactRepository repository) {
        IO.println("\n-- Search by Name --");
        String term = readString("  Search term: ");

        List<Contact> results = repository.findByName(term);
        if (results.isEmpty()) {
            IO.println("  No contacts found matching \"" + term + "\".");
            return;
        }

        printHeader();
        for (Contact c : results) {
            IO.println("  " + c);
        }
    }

    // -------------------------------------------------------------------------
    // Option 4 – Search by email (exact, case-insensitive)
    // -------------------------------------------------------------------------

    private static void searchByEmail(ContactRepository repository) {
        IO.println("\n-- Search by Email --");
        String email = readString("  Email: ");

        Optional<Contact> result = repository.findByEmail(email);
        if (result.isEmpty()) {
            IO.println("  No contact found with email \"" + email + "\".");
            return;
        }

        printHeader();
        IO.println("  " + result.get());
    }

    // -------------------------------------------------------------------------
    // Option 5 – Update an existing contact
    // -------------------------------------------------------------------------

    private static void updateContact(ContactRepository repository) {
        IO.println("\n-- Update Contact --");
        int id = readInt("  Enter the ID of the contact to update: ");

        Optional<Contact> existing = repository.findById(id);
        if (existing.isEmpty()) {
            IO.println("  No contact found with ID " + id + ".");
            return;
        }

        Contact contact = existing.get();
        IO.println("  Current: " + contact);
        IO.println("  (Press Enter to keep the current value)");

        // Read new values; fall back to the current value when the user presses Enter
        contact.name  = readOptional("  New name  [" + contact.name  + "]: ", contact.name);
        contact.email = readOptional("  New email [" + contact.email + "]: ", contact.email);
        contact.phone = readOptional("  New phone [" + contact.phone + "]: ", contact.phone);

        try {
            repository.update(contact);
            IO.println("  Updated: " + contact);
        } catch (IllegalArgumentException e) {
            IO.println("  Error: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Option 6 – Delete a contact
    // -------------------------------------------------------------------------

    private static void deleteContact(ContactRepository repository) {
        IO.println("\n-- Delete Contact --");
        int id = readInt("  Enter the ID of the contact to delete: ");

        Optional<Contact> existing = repository.findById(id);
        if (existing.isEmpty()) {
            IO.println("  No contact found with ID " + id + ".");
            return;
        }

        IO.println("  About to delete: " + existing.get());
        String confirm = readString("  Are you sure? (yes/no): ");

        if (!confirm.equalsIgnoreCase("yes")) {
            IO.println("  Deletion cancelled.");
            return;
        }

        repository.deleteById(id);
        IO.println("  Contact deleted.");
    }

    // -------------------------------------------------------------------------
    // Input helpers — centralise all IO calls here (DRY)
    // -------------------------------------------------------------------------

    /** Prompts for a non-empty string. Re-prompts if the user presses Enter with nothing. */
    private static String readString(String prompt) {
        String input;
        do {
            input = IO.readln(prompt).trim();
        } while (input.isEmpty());
        return input;
    }

    /** Prompts for a string; returns the fallback value when the user just presses Enter. */
    private static String readOptional(String prompt, String fallback) {
        String input = IO.readln(prompt).trim();
        return input.isEmpty() ? fallback : input;
    }

    /** Prompts for an integer; re-prompts on invalid input instead of crashing. */
    private static int readInt(String prompt) {
        while (true) {
            String line = IO.readln(prompt).trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                IO.println("  Please enter a whole number.");
            }
        }
    }

    // -------------------------------------------------------------------------
    // Display helpers
    // -------------------------------------------------------------------------

    /** Prints a column header that aligns with Contact.toString(). */
    private static void printHeader() {
        IO.println(String.format("  %-4s %-20s | %-30s | %s", "ID", "Name", "Email", "Phone"));
        IO.println("  " + "-".repeat(70));
    }
}
