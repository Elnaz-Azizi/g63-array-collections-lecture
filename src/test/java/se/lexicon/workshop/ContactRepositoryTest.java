package se.lexicon.workshop;

import org.junit.jupiter.api.*;
import se.lexicon.workshop.contact.Contact;
import se.lexicon.workshop.contact.ContactRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ContactRepository (Challenge 2).
 *
 * Each CRUD operation has at least:
 *   - one positive test (the happy path)
 *   - one negative test (invalid input or constraint violation)
 *
 * A fresh repository is created before every test so tests are fully independent.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ContactRepositoryTest {

    // A clean repository and sample contacts are rebuilt before each test
    private ContactRepository repository;
    private Contact alice;
    private Contact bob;

    @BeforeEach
    void setUp() {
        repository = new ContactRepository();
        // Add two starter contacts so tests that read/update/delete have data to work with
        alice = repository.add(new Contact("Alice", "alice@example.com", "070-111 1111"));
        bob   = repository.add(new Contact("Bob",   "bob@example.com",   "070-222 2222"));
    }

    // =========================================================================
    // add()
    // =========================================================================

    @Test
    @Order(1)
    @DisplayName("add — positive: new contact is stored and returned")
    void add_storesAndReturnsContact() {
        Contact carol = repository.add(new Contact("Carol", "carol@example.com", ""));
        assertNotNull(carol, "add() should return the stored contact.");
        assertEquals(3, repository.size(), "Repository should now hold 3 contacts.");
    }

    @Test
    @Order(2)
    @DisplayName("add — negative: duplicate email throws IllegalArgumentException")
    void add_duplicateEmail_throwsException() {
        // alice@example.com is already in the repository from setUp()
        assertThrows(IllegalArgumentException.class,
                () -> repository.add(new Contact("Alice2", "alice@example.com", "")),
                "Adding a contact with a duplicate email should throw.");
    }

    // =========================================================================
    // findAll()
    // =========================================================================

    @Test
    @Order(3)
    @DisplayName("findAll — positive: returns all contacts sorted A-Z by name")
    void findAll_returnsSortedContacts() {
        List<Contact> all = repository.findAll();
        assertEquals(2, all.size());
        // Alice comes before Bob alphabetically
        assertEquals("Alice", all.get(0).name);
        assertEquals("Bob",   all.get(1).name);
    }

    @Test
    @Order(4)
    @DisplayName("findAll — negative: returns empty list when repository is empty")
    void findAll_emptyRepository_returnsEmptyList() {
        ContactRepository emptyRepo = new ContactRepository();
        assertTrue(emptyRepo.findAll().isEmpty(), "findAll() on an empty repo should return an empty list.");
    }

    // =========================================================================
    // findByName()
    // =========================================================================

    @Test
    @Order(5)
    @DisplayName("findByName — positive: partial, case-insensitive match returns correct contact")
    void findByName_partialMatch_returnsContact() {
        List<Contact> results = repository.findByName("ali");
        assertEquals(1, results.size());
        assertEquals("Alice", results.get(0).name);
    }

    @Test
    @Order(6)
    @DisplayName("findByName — negative: no match returns empty list")
    void findByName_noMatch_returnsEmptyList() {
        List<Contact> results = repository.findByName("zzz");
        assertTrue(results.isEmpty(), "A search with no matches should return an empty list.");
    }

    // =========================================================================
    // findByEmail()
    // =========================================================================

    @Test
    @Order(7)
    @DisplayName("findByEmail — positive: exact match (case-insensitive) returns contact")
    void findByEmail_exactMatch_returnsContact() {
        Optional<Contact> result = repository.findByEmail("BOB@EXAMPLE.COM");
        assertTrue(result.isPresent(), "findByEmail should match regardless of case.");
        assertEquals("Bob", result.get().name);
    }

    @Test
    @Order(8)
    @DisplayName("findByEmail — negative: unknown email returns Optional.empty")
    void findByEmail_unknownEmail_returnsEmpty() {
        Optional<Contact> result = repository.findByEmail("nobody@example.com");
        assertTrue(result.isEmpty(), "findByEmail should return empty for an unknown address.");
    }

    // =========================================================================
    // update()
    // =========================================================================

    @Test
    @Order(9)
    @DisplayName("update — positive: changed fields are persisted")
    void update_validChange_returnsTrue() {
        alice.name  = "Alice Updated";
        alice.phone = "070-999 9999";

        boolean success = repository.update(alice);

        assertTrue(success, "update() should return true when the contact exists.");
        // Verify the change is visible through a subsequent find
        Optional<Contact> found = repository.findById(alice.id);
        assertTrue(found.isPresent());
        assertEquals("Alice Updated", found.get().name);
    }

    @Test
    @Order(10)
    @DisplayName("update — negative: using another contact's email throws IllegalArgumentException")
    void update_emailCollision_throwsException() {
        // Add a third contact so alice's stored email stays untouched.
        // Mutating alice directly would change the stored reference, making the
        // collision check compare alice against herself (same id → no throw).
        Contact carol = repository.add(new Contact("Carol", "carol@example.com", ""));

        // Now try to reassign carol's email to alice's already-taken address
        carol.email = alice.email;

        assertThrows(IllegalArgumentException.class,
                () -> repository.update(carol),
                "update() should reject an email that belongs to a different contact.");
    }

    // =========================================================================
    // deleteById()
    // =========================================================================

    @Test
    @Order(11)
    @DisplayName("deleteById — positive: contact is removed from the repository")
    void deleteById_existingId_returnsTrue() {
        boolean removed = repository.deleteById(alice.id);
        assertTrue(removed, "deleteById() should return true when the id exists.");
        assertEquals(1, repository.size(), "Only Bob should remain.");
    }

    @Test
    @Order(12)
    @DisplayName("deleteById — negative: unknown id returns false")
    void deleteById_unknownId_returnsFalse() {
        boolean removed = repository.deleteById(9999);
        assertFalse(removed, "deleteById() should return false for an id that does not exist.");
        assertEquals(2, repository.size(), "No contact should have been removed.");
    }
}
