package se.lexicon.workshop.contact;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves and loads contacts as a JSON file.
 *
 * Why Gson?
 *   - One method to serialise: gson.toJson(list, writer)
 *   - One method to deserialise: gson.fromJson(reader, type)
 *   - No annotations or configuration required for simple POJOs
 *
 * Why JSON?
 *   - Human-readable — you can open the file and inspect or edit it
 *   - Widely supported — the same format used by REST APIs and most tools
 *
 * File location: contacts.json in the working directory (project root when run from IntelliJ).
 */
public class ContactFileStorage {

    static final String FILE_PATH = "contacts.json";

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()   // writes indented, readable JSON
            .create();

    // -------------------------------------------------------------------------
    // Save
    // -------------------------------------------------------------------------

    /**
     * Writes all contacts to contacts.json.
     * The file is created if it does not exist; overwritten if it does.
     */
    static void save(List<Contact> contacts) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            GSON.toJson(contacts, writer);
        } catch (IOException e) {
            IO.println("Warning: could not save contacts — " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Load
    // -------------------------------------------------------------------------

    /**
     * Reads contacts from contacts.json.
     * Returns an empty list when the file does not exist yet (first run).
     * After loading, resets the Contact ID counter so new contacts get
     * IDs above the highest loaded ID.
     */
    static List<Contact> load() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Contact>>() {}.getType();
            List<Contact> loaded = GSON.fromJson(reader, listType);

            if (loaded == null || loaded.isEmpty()) return new ArrayList<>();

            // Restore the ID counter so the next new contact doesn't collide
            int maxId = loaded.stream().mapToInt(c -> c.id).max().orElse(0);
            Contact.resetCounter(maxId);

            return loaded;

        } catch (IOException e) {
            IO.println("Warning: could not load contacts — " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
