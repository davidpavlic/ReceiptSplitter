package ch.zhaw.it.pm2.receiptsplitter.model;

import ch.zhaw.it.pm2.receiptsplitter.model.ContactReceiptItem.ContactReceiptItemErrorMessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactReceiptItemTest {

    private static final float VALID_PRICE = 3.95F;
    private static final String VALID_NAME = "Coke";

    @Mock
    private Contact mockContact;

    private ContactReceiptItem contactReceiptItem;

    @BeforeEach
    void setUp() {
        lenient().when(mockContact.getFirstName()).thenReturn("Pablo");
        lenient().when(mockContact.getLastName()).thenReturn("Escobar");
        lenient().when(mockContact.getEmail()).thenReturn("Pablo.Escobar@hotmail.mx");
        contactReceiptItem = new ContactReceiptItem(VALID_PRICE, VALID_NAME, mockContact);
    }

    @Test
    void constructor_ValidAttributes_ItemCreated() {
        //Assert
        assertContactReceiptItemAttributes(VALID_PRICE, VALID_NAME, mockContact);
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
        Contact newMockContact = mock(Contact.class);

        //Act
        contactReceiptItem.setContact(newMockContact);

        //Assert
        assertEquals(newMockContact, contactReceiptItem.getContact());
    }

    @ParameterizedTest
    @ValueSource(floats = {-1.0F, 0F})
    void setPrice_InvalidAttributes_ThrowsException(float price) {
        //Arrange & Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new ContactReceiptItem(price, VALID_NAME, mockContact));
        assertEquals(ContactReceiptItemErrorMessageType.PRICE_ZERO_OR_LOWER.toString(), exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "  "})
    void setName_InvalidAttributes_ThrowsException(String name) {
        //Arrange & Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new ContactReceiptItem(VALID_PRICE, name, mockContact));
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