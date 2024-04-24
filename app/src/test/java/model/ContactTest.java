package model;

import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//TODO: Add Mocking because all test are dependent on correct constructor initialization
public class ContactTest {

    private static final String VALID_FIRSTNAME = "Max";
    private static final String VALID_LASTNAME = "Mustermann";
    private static final String VALID_EMAIL = "MaxMustermann@hotmail.com";
    private Contact contact;

    @BeforeEach
    void setUp() {
        contact = new Contact(VALID_FIRSTNAME, VALID_LASTNAME, VALID_EMAIL);
    }

    @Test
    void createValidItem() {
        assertEquals(VALID_FIRSTNAME, contact.getFirstName());
        assertEquals(VALID_LASTNAME, contact.getLastName());
        assertEquals(VALID_EMAIL, contact.getEmail());
    }

    @Test
    void updateExistingItem() {
        String newFirstName = "Hans";
        String newLastName = "Landa";
        String newEmail = "HansLanda@hotmail.com";

        contact.setFirstName(newFirstName);
        contact.setLastName(newLastName);
        contact.setEmail(newEmail);

        assertEquals(newFirstName, contact.getFirstName());
        assertEquals(newLastName, contact.getLastName());
        assertEquals(newEmail, contact.getEmail());

        contact.setLastName("");
        assertEquals("", contact.getLastName());
    }

    @Test
    void invalidFirstNameTest() {
        Exception exception;

        exception = assertThrows(IllegalArgumentException.class, () -> new Contact(null, VALID_LASTNAME, VALID_EMAIL));
        assertEquals("First name must not be empty.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> new Contact("", VALID_LASTNAME, VALID_EMAIL));
        assertEquals("First name must not be empty.", exception.getMessage());
    }

    @Test
    void invalidLastNameTest() {
        Exception exception;

        exception= assertThrows(IllegalArgumentException.class, () -> new Contact(VALID_FIRSTNAME, null, VALID_EMAIL));
        assertEquals("Last name must not be null.", exception.getMessage());
    }

    @Test
    void invalidEmailTest() {
        Exception exception;

        exception = assertThrows(IllegalArgumentException.class, () -> new Contact(VALID_FIRSTNAME, VALID_LASTNAME, null));
        assertEquals("Email must be a valid email address.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> new Contact(VALID_FIRSTNAME, VALID_LASTNAME, ""));
        assertEquals("Email must be a valid email address.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> new Contact(VALID_FIRSTNAME, VALID_LASTNAME, "johndoe.example.com"));
        assertEquals("Email must be a valid email address.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> new Contact(VALID_FIRSTNAME, VALID_LASTNAME, "john@doe@example.com"));
        assertEquals("Email must be a valid email address.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> new Contact(VALID_FIRSTNAME, VALID_LASTNAME, "john.doe@"));
        assertEquals("Email must be a valid email address.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> new Contact(VALID_FIRSTNAME, VALID_LASTNAME, "john.doe@example"));
        assertEquals("Email must be a valid email address.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> new Contact(VALID_FIRSTNAME, VALID_LASTNAME, "john.doe@example.c"));
        assertEquals("Email must be a valid email address.", exception.getMessage());
    }
}
