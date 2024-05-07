package ch.zhaw.it.pm2.receiptsplitter.model;

import ch.zhaw.it.pm2.receiptsplitter.model.Contact.ContactErrorMessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void constructor_ValidAttributes_ItemCreated() {
        //Assert
        assertContactAttributes(VALID_FIRSTNAME, VALID_LASTNAME, VALID_EMAIL);
    }

    @Test
    void setFirstName_ValidAttributes_FirstNameUpdated() {
        //Arrange
        String newFirstName = "Hans";

        //Act
        contact.setFirstName(newFirstName);

        //Assert
        assertEquals(newFirstName, contact.getFirstName());
    }

    @Test
    void setLastName_ValidAttributes_LastNameUpdated() {
        //Arrange
        String newLastName = "Landa";

        //Act
        contact.setLastName(newLastName);

        //Assert
        assertEquals(newLastName, contact.getLastName());
    }

    @Test
    void setEmail_ValidAttributes_EmailUpdated() {
        //Arrange
        String newEmail = "HansLanda@hotmail.com";

        //Act
        contact.setEmail(newEmail);

        //Assert
        assertEquals(newEmail, contact.getEmail());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "  "})
    void setFirstName_InvalidAttributes_ThrowsException(String firstName) {
        //Arrange & Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Contact(firstName, VALID_LASTNAME, VALID_EMAIL));
        assertEquals(ContactErrorMessageType.FIRST_NAME_EMPTY.toString(), exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void setLastName_InvalidAttributes_ThrowsException(String lastName) {
        //Arrange & Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Contact(VALID_FIRSTNAME, lastName, VALID_EMAIL));
        assertEquals(ContactErrorMessageType.LAST_NAME_NULL.toString(), exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("getInvalidEmailFormats")
    void setEmail_InvalidAttributes_ThrowsException(String email) {
        //Arrange & Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Contact(VALID_FIRSTNAME, VALID_LASTNAME, email));
        assertEquals(ContactErrorMessageType.EMAIL_INVALID.toString(), exception.getMessage());
    }

    private void assertContactAttributes(String firstname, String lastname, String email){
        assertEquals(firstname, contact.getFirstName());
        assertEquals(lastname, contact.getLastName());
        assertEquals(email, contact.getEmail());
    }

    private static String[] getInvalidEmailFormats(){
        return new String[]{"",
                            "  ",
                            "johndoe.example.com",
                            "john@doe@example.com",
                            "john.doe@",
                            "john.doe@example",
                            "john.doe@example.c"};
    }
}
