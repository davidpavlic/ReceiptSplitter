package model;

import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//TODO: Add Mocking because all test are dependent on correct constructor initialization
//TODO: Enum for error messages?
public class ContactTest {

    private static final String VALID_FIRSTNAME = "Max";
    private static final String VALID_LASTNAME = "Mustermann";
    private static final String VALID_EMAIL = "MaxMustermann@hotmail.com";
    private Contact contact;

    private static final String FIRST_NAME_ERROR_MESSAGE   = "First name must not be empty.";
    private static final String LAST_NAME_ERROR_MESSAGE    = "Last name must not be null.";
    private static final String EMAIL_ERROR_MESSAGE        = "Email must be a valid email address.";

    @BeforeEach
    void setUp() {
        contact = new Contact(VALID_FIRSTNAME, VALID_LASTNAME, VALID_EMAIL);
    }

    @Test
    void whenCreatingValidItem_thenAllAttributesAreCorrect() {
        assertContactAttributes(VALID_FIRSTNAME, VALID_LASTNAME, VALID_EMAIL);
    }

    @Test
    void whenUpdatingItem_thenAllAttributesAreUpdated() {
        String newFirstName = "Hans";
        String newLastName = "Landa";
        String newEmail = "HansLanda@hotmail.com";

        contact.setFirstName(newFirstName);
        contact.setLastName(newLastName);
        contact.setEmail(newEmail);

        assertContactAttributes(newFirstName, newLastName, newEmail);

        contact.setLastName("");
        assertEquals("", contact.getLastName());
    }

    private void assertContactAttributes(String firstname, String lastname, String email){
        assertEquals(firstname, contact.getFirstName());
        assertEquals(lastname, contact.getLastName());
        assertEquals(email, contact.getEmail());

    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "  "})
    void givenInvalidFirstName_whenCreatingItem_thenThrowsException(String firstName) {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Contact(firstName, VALID_LASTNAME, VALID_EMAIL));
        assertEquals(FIRST_NAME_ERROR_MESSAGE, exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void givenInvalidLastName_whenCreatingItem_thenThrowsException(String lastName) {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Contact(VALID_FIRSTNAME, lastName, VALID_EMAIL));
        assertEquals(LAST_NAME_ERROR_MESSAGE, exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "  ", "johndoe.example.com", "john@doe@example.com", "john.doe@", "john.doe@example", "john.doe@example.c"})
    void givenInvalidEmail_whenCreatingItem_thenThrowsException(String email) {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Contact(VALID_FIRSTNAME, VALID_LASTNAME, email));
        assertEquals(EMAIL_ERROR_MESSAGE, exception.getMessage());
    }
}
