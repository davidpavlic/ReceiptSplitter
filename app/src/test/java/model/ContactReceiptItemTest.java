package model;

import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.model.ContactReceiptItem;
import ch.zhaw.it.pm2.receiptsplitter.model.ContactReceiptItem.ContactReceiptItemErrorMessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//TODO: Add Mocking because all test are dependent on correct constructor initialization
public class ContactReceiptItemTest {

    private static final float VALID_PRICE = 3.95F;
    private static final String VALID_NAME = "Coke";
    private static final Contact VALID_CONTACT = new Contact("Pablo", "Escobar", "Pablo.Escobar@hotmail.mx");
    private ContactReceiptItem contactReceiptItem;

    @BeforeEach
    void setUp() {
        contactReceiptItem = new ContactReceiptItem(VALID_PRICE, VALID_NAME, VALID_CONTACT);
    }

    @Test
    void constructor_ValidAttributes_ItemCreated() {
        //TODO: Set Arrange and Act by including mocking and moving setup-method here
        //Assert
        assertContactReceiptItemAttributes(VALID_PRICE, VALID_NAME, VALID_CONTACT);
    }

    @Test
    void setPrice_ValidAttributes_PriceUpdated() {
        //Arrange
        float newPrice = 5.95F;

        //Act
        contactReceiptItem.setPrice(newPrice);

        //Assert
        assertEquals(newPrice, contactReceiptItem.getPrice());
    }

    @Test
    void setName_ValidAttributes_NameUpdated() {
        //Arrange
        String newName = "Burger";

        //Act
        contactReceiptItem.setName(newName);

        //Assert
        assertEquals(newName, contactReceiptItem.getName());
    }

    @Test
    void setContact_ValidAttributes_ContactUpdated() {
        //Arrange
        Contact newContact = new Contact("Ronald", "McDonald", "Ronald.McDonald@megges.com");

        //Act
        contactReceiptItem.setContact(newContact);

        //Assert
        assertEquals(newContact, contactReceiptItem.getContact());
    }

    @ParameterizedTest
    @ValueSource(floats = {-1.0F, 0F})
    void setPrice_InvalidAttributes_ThrowsException(float price) {
        //Arrange & Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new ContactReceiptItem(price, VALID_NAME, VALID_CONTACT));
        assertEquals(ContactReceiptItemErrorMessageType.PRICE_ZERO_OR_LOWER.toString(), exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "  "})
    void setName_InvalidAttributes_ThrowsException(String name) {
        //Arrange & Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new ContactReceiptItem(VALID_PRICE, name, VALID_CONTACT));
        assertEquals(ContactReceiptItemErrorMessageType.NAME_EMPTY.toString(), exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void setContact_InvalidAttributes_ThrowsException(Contact contact) {
        //Arrange & Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new ContactReceiptItem(VALID_PRICE, VALID_NAME, contact));
        assertEquals(ContactReceiptItemErrorMessageType.CONTACT_NULL.toString(), exception.getMessage());
    }

    private void assertContactReceiptItemAttributes(float expectedPrice, String expectedName, Contact expectedContact) {
        assertEquals(expectedPrice, contactReceiptItem.getPrice());
        assertEquals(expectedName, contactReceiptItem.getName());
        assertEquals(expectedContact, contactReceiptItem.getContact());
    }
}