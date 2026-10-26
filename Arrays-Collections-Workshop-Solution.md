![Lexicon Logo](https://lexicongruppen.se/media/wi5hphtd/lexicon-logo.svg)

# Solution – Contact Management App

---

## Architecture Overview

The app is split into four classes, each with one clear responsibility.

```
ContactApp           — UI layer: reads input, prints output, drives the menu loop
ContactRepository    — Data layer: stores contacts in memory, enforces business rules
ContactFileStorage   — Persistence layer: saves and loads contacts as JSON
Contact              — Model: holds the data for one contact
```

This is **Separation of Concerns** in practice. No class does more than one job:
- `ContactApp` never talks to the file system directly
- `ContactRepository` never prints anything to the console
- `ContactFileStorage` never validates business rules
- `Contact` never knows it is being stored or displayed

Each class can be changed without touching the others.

---

## Contact.java — The Model

**What it is:** A plain data class that represents one contact: an ID, a name, an email, and an optional phone number.

**Key design decisions:**

### Auto-incrementing ID

```java
private static int nextId = 1;

public Contact(String name, String email, String phone) {
    this.id    = nextId++;
    this.name  = name;
    this.email = email;
    this.phone = (phone != null) ? phone.trim() : "";
}
```

`nextId` is a `static` field — it belongs to the class, not to any one instance. Every time a new `Contact` is created, `nextId++` assigns the current value to `id` and then increments it. This guarantees every contact gets a unique ID automatically, without the caller having to track numbers.

### Public fields

Fields are `public` so they can be read and updated directly (`contact.name = "..."`) rather than through getters and setters. For a workshop-scope app this keeps the code shorter and more readable. In a production system, you would typically hide fields behind getters and setters to control access.

### ID counter reset after loading

```java
static void resetCounter(int maxId) {
    nextId = maxId + 1;
}
```

When contacts are loaded from a file, Gson reconstructs the objects without calling the constructor, so `nextId` is never incremented. Without this reset, new contacts would start at ID 1 again and collide with loaded IDs. `ContactFileStorage` calls this after loading to set `nextId` above the highest loaded ID.

### equals and hashCode by ID

```java
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
```

Two contacts are considered the same if they have the same ID — not the same name or email, because those can change when the contact is updated. This is the correct definition of identity for this model.

> **Rule:** whenever you override `equals()`, always override `hashCode()` too. If two objects are equal, they must produce the same hash code. Failing to do this breaks `HashMap`, `HashSet`, and any other hash-based collection.

### Formatted toString

```java
@Override
public String toString() {
    String phoneDisplay = (phone == null || phone.isEmpty()) ? "(no phone)" : phone;
    return String.format("[%d] %-20s | %-30s | %s", id, name, email, phoneDisplay);
}
```

`String.format` with width specifiers (`%-20s`) left-aligns each field in a fixed-width column. This makes the output line up in a table when multiple contacts are printed — without any extra effort from the caller.

---

## ContactRepository.java — The Data Layer

**What it is:** The single source of truth for all contacts in memory. It enforces the rule that no two contacts can share an email address.

**Why `ArrayList`?**

```java
private final ArrayList<Contact> contacts = new ArrayList<>();
```

- Contacts are searched by field value (name, email, ID) — not by key — so a `Map` would not simplify lookups.
- Contacts need to be iterated in order and listed as a sequence — a `Set` does not provide a predictable order.
- `ArrayList` gives O(1) access by index and straightforward CRUD with no overhead.

### Add — with unique-email constraint

```java
public Contact add(Contact contact) {
    if (contact == null) {
        throw new IllegalArgumentException("Contact must not be null.");
    }
    if (findByEmail(contact.email).isPresent()) {
        throw new IllegalArgumentException(
            "A contact with email '" + contact.email + "' already exists.");
    }
    contacts.add(contact);
    return contact;
}
```

The duplicate-email check runs before inserting. If `findByEmail` returns a value, the add is rejected with an `IllegalArgumentException`. `ContactApp` catches this and shows the message to the user without crashing.

### findAll — defensive copy + sorted

```java
public List<Contact> findAll() {
    List<Contact> sorted = new ArrayList<>(contacts);
    sorted.sort(Comparator.comparing(c -> c.name.toLowerCase()));
    return Collections.unmodifiableList(sorted);
}
```

- A new list is created from `contacts` — this is a *defensive copy*. If the caller modifies the returned list, the internal store is unaffected.
- `Collections.unmodifiableList()` wraps the copy so any mutation attempt throws an exception at runtime, making the intent explicit.
- Sorting is done on a lowercase copy of the name so `"alice"` and `"Alice"` sort in the same position — case-insensitive alphabetical order.

### findByName — partial, case-insensitive

```java
public List<Contact> findByName(String namePart) {
    String term = namePart.trim().toLowerCase();
    List<Contact> results = new ArrayList<>();
    for (Contact c : contacts) {
        if (c.name.toLowerCase().contains(term)) {
            results.add(c);
        }
    }
    return Collections.unmodifiableList(results);
}
```

`contains(term)` matches anywhere in the name, not just at the start. Searching for `"son"` would match `"Johnson"`, `"Svensson"`, and `"Jonsson"`. Both sides are lowercased before comparing so capitalisation does not matter.

### findByEmail and findById — returning Optional

```java
public Optional<Contact> findByEmail(String email) { ... }
public Optional<Contact> findById(int id) { ... }
```

Both methods return `Optional<Contact>` instead of `Contact` or `null`. This forces the caller to acknowledge the possibility of "not found" at compile time — you cannot accidentally call `.name` on an `Optional` the way you can on `null`. In `ContactApp`, callers check `result.isEmpty()` before proceeding.

> **`Optional` in plain English:** A box that either contains a value or is empty. You must open the box (`get()`, `isPresent()`, `isEmpty()`) before using the value — the compiler reminds you not to skip this step.

### Update — email-theft guard

```java
public boolean update(Contact updated) {
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
    return false;
}
```

The guard allows a contact to keep its own email (same ID) but rejects assigning an email that belongs to a *different* contact. Without the `id != updated.id` check, updating a contact without changing its email would always fail.

### Delete — removeIf

```java
public boolean deleteById(int id) {
    return contacts.removeIf(c -> c.id == id);
}
```

`removeIf` scans the list and removes every element that matches the predicate, returning `true` if anything was removed. One line replaces what would otherwise be an iterator loop with a manual `remove()` call.

### loadAll — bulk insert for file loading

```java
void loadAll(List<Contact> loaded) {
    contacts.addAll(loaded);
}
```

This is package-private and used only by `ContactFileStorage`. It bypasses the duplicate-email check because data coming from the file was already validated when it was saved — there is no need to re-check it. Re-running the check would also cause false failures if `findByEmail` was called while the list was only partially populated.

---

## ContactFileStorage.java — JSON Persistence

**What it is:** A utility class with two methods — `save` and `load`. All file I/O lives here and nowhere else.

**Why JSON?**
- Human-readable: you can open `contacts.json` in any text editor to inspect or manually edit the data.
- Widely supported: it is the same format used by REST APIs, databases, and configuration files across every platform.

**Why Gson?**
- Zero configuration for plain Java classes — no annotations required.
- Two lines to serialise, two lines to deserialise.
- `setPrettyPrinting()` produces indented, readable output.

### Gson setup

```java
private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();
```

`GsonBuilder` is the configuration step. `create()` produces the final `Gson` instance. We store it as a `static final` constant — one instance is shared across all calls, which is fine because `Gson` is thread-safe and stateless.

### Save

```java
static void save(List<Contact> contacts) {
    try (Writer writer = new FileWriter(FILE_PATH)) {
        GSON.toJson(contacts, writer);
    } catch (IOException e) {
        IO.println("Warning: could not save contacts — " + e.getMessage());
    }
}
```

`GSON.toJson(contacts, writer)` serialises the entire list directly to the file in one call. The `try-with-resources` block (`try (Writer writer = ...)`) ensures the file is closed automatically when the block exits — even if an exception is thrown. This prevents file handle leaks.

The output in `contacts.json` looks like:

```json
[
  {
    "id": 1,
    "name": "Mehrdad Javan",
    "email": "mehrdad@lexicon.se",
    "phone": "0712345678"
  },
  {
    "id": 2,
    "name": "Elnaz Azizi",
    "email": "elnaz@lexicon.se",
    "phone": ""
  }
]
```

### Load

```java
static List<Contact> load() {
    File file = new File(FILE_PATH);
    if (!file.exists()) return new ArrayList<>();

    try (Reader reader = new FileReader(file)) {
        Type listType = new TypeToken<ArrayList<Contact>>() {}.getType();
        List<Contact> loaded = GSON.fromJson(reader, listType);

        if (loaded == null || loaded.isEmpty()) return new ArrayList<>();

        int maxId = loaded.stream().mapToInt(c -> c.id).max().orElse(0);
        Contact.resetCounter(maxId);

        return loaded;
    } catch (IOException e) {
        IO.println("Warning: could not load contacts — " + e.getMessage());
        return new ArrayList<>();
    }
}
```

**`TypeToken`** — Gson needs to know the exact generic type at runtime. Java erases generic type information at compile time (type erasure), so `List<Contact>.class` does not exist. `new TypeToken<ArrayList<Contact>>() {}.getType()` is Gson's workaround: it creates an anonymous subclass to preserve the generic type, then extracts it via reflection.

**ID counter reset** — Gson reconstructs `Contact` objects by setting fields directly, bypassing the constructor. This means `nextId` is never incremented during loading. After loading, we find the highest ID in the loaded list and call `Contact.resetCounter(maxId)` so the next new contact gets `maxId + 1`, not `1`.

> **`try-with-resources`** works with any class that implements `AutoCloseable` — `FileWriter`, `FileReader`, database connections, HTTP clients, and more. Always use it when opening a resource that needs to be closed.

---

## ContactApp.java — The UI Layer

**What it is:** The entry point. It drives the menu loop, reads user input, and delegates every action to `ContactRepository`. It does not contain any business logic.

### Startup and shutdown

```java
void main() {
    ContactRepository repository = new ContactRepository();

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
        switch (choice) { ... }
    }

    ContactFileStorage.save(repository.findAll());
    IO.println("Contacts saved to " + ContactFileStorage.FILE_PATH + ".");
    IO.println("Goodbye!");
}
```

Load happens before the loop — the user sees their previous contacts immediately. Save happens after the loop exits — when the user chooses `0`, the loop ends and the data is persisted before the program terminates.

### Switch expression for the menu

```java
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
```

The arrow (`->`) form of `switch` does not fall through between cases and does not need `break`. Each arm executes exactly one thing. This is cleaner and safer than the classic `switch` with `break` statements.

### Input helpers — DRY

Three private helpers centralise all user input so the rest of the class never calls `IO.readln` directly:

```java
// Forces a non-empty answer — re-prompts on blank input
private static String readString(String prompt) {
    String input;
    do {
        input = IO.readln(prompt).trim();
    } while (input.isEmpty());
    return input;
}

// Optional input — returns the fallback when the user just presses Enter
private static String readOptional(String prompt, String fallback) {
    String input = IO.readln(prompt).trim();
    return input.isEmpty() ? fallback : input;
}

// Reads an integer — re-prompts on non-numeric input instead of crashing
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
```

`readOptional` is used in the update flow. The prompt shows the current value in brackets (`New name [Mehrdad]: `). If the user presses Enter without typing, the field keeps its existing value. Only fields where the user actually types something are updated.

### Update flow

```java
Contact contact = existing.get();
IO.println("  Current: " + contact);
IO.println("  (Press Enter to keep the current value)");

contact.name  = readOptional("  New name  [" + contact.name  + "]: ", contact.name);
contact.email = readOptional("  New email [" + contact.email + "]: ", contact.email);
contact.phone = readOptional("  New phone [" + contact.phone + "]: ", contact.phone);

repository.update(contact);
```

We mutate the existing `Contact` object in place, then call `repository.update(contact)` to save the changes back. Because `Contact` fields are `public`, no setters are needed. The repository's update method matches by ID to find the right entry.

### Delete with confirmation

```java
IO.println("  About to delete: " + existing.get());
String confirm = readString("  Are you sure? (yes/no): ");

if (!confirm.equalsIgnoreCase("yes")) {
    IO.println("  Deletion cancelled.");
    return;
}

repository.deleteById(id);
```

The contact is shown before confirming so the user knows exactly what they are deleting. `equalsIgnoreCase("yes")` accepts `"YES"`, `"Yes"`, and `"yes"`. Anything other than `yes` cancels the operation.

---

## How the Pieces Connect

```
User types input
       ↓
  ContactApp          reads input with IO.readln
       ↓               delegates to →   ContactRepository   (stores in ArrayList)
  ContactApp          prints output with IO.println
       ↑
  On startup:   ContactFileStorage.load()  → ContactRepository.loadAll()
  On exit:      ContactFileStorage.save()  ← ContactRepository.findAll()
```

---

## Programming Principles Applied

| Principle | Where |
|---|---|
| **SRP** | Each class has one job — model, storage, data layer, UI |
| **Separation of Concerns** | `ContactApp` never touches files; `ContactRepository` never prints |
| **DRY** | All input reading goes through three shared helpers in `ContactApp` |
| **KISS** | `ArrayList` for storage — the simplest structure that fits the need |
| **YAGNI** | No caching, no pagination, no export formats beyond what was asked for |
