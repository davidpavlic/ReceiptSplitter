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
        assertEquals(Contact.ContactErrorMessageType.FIRST_NAME_EMPTY.toString(), exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void givenInvalidLastName_whenCreatingItem_thenThrowsException(String lastName) {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Contact(VALID_FIRSTNAME, lastName, VALID_EMAIL));
        assertEquals(Contact.ContactErrorMessageType.LAST_NAME_NULL.toString(), exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "  ", "johndoe.example.com", "john@doe@example.com", "john.doe@", "john.doe@example", "john.doe@example.c"})
    void givenInvalidEmail_whenCreatingItem_thenThrowsException(String email) {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Contact(VALID_FIRSTNAME, VALID_LASTNAME, email));
        assertEquals(Contact.ContactErrorMessageType.EMAIL_INVALID.toString(), exception.getMessage());
    }
}
