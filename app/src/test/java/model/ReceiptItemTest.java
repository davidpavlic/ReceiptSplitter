package model;

import ch.zhaw.it.pm2.receiptsplitter.model.ReceiptItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//TODO: Add Mocking because all test are dependent on correct constructor initialization
//TODO: Enum for error messages?
public class ReceiptItemTest {

    private static final float VALID_PRICE = 9.95F;
    private static final String VALID_NAME = "Coca-Cola";
    private static final int VALID_AMOUNT = 3;
    private ReceiptItem receiptItem;

    private static final String NAME_ERROR_MESSAGE     = "Name must not be empty.";
    private static final String PRICE_ERROR_MESSAGE    = "Price must not be zero or lower.";
    private static final String AMOUNT_ERROR_MESSAGE   = "Amount must not be zero or lower.";

    @BeforeEach
    void setUp() {
        receiptItem = new ReceiptItem(VALID_PRICE, VALID_NAME, VALID_AMOUNT);
    }

    @Test
    void whenCreatingValidItem_thenAllAttributesAreCorrect() {
        assertReceiptItemAttributes(VALID_PRICE, VALID_NAME, VALID_AMOUNT);
    }

    @Test
    void whenUpdatingItem_thenAllAttributesAreUpdated() {
        float newPrice = 4.95F;
        String newName = "Vivi Cola";
        int newAmount = 2;

        receiptItem.setPrice(newPrice);
        receiptItem.setName(newName);
        receiptItem.setAmount(newAmount);

        assertReceiptItemAttributes(newPrice, newName, newAmount);
    }

    private void assertReceiptItemAttributes(float price, String name, int amount) {
        assertEquals(price, receiptItem.getPrice());
        assertEquals(name, receiptItem.getName());
        assertEquals(amount, receiptItem.getAmount());
    }

    @ParameterizedTest
    @ValueSource(floats = {-1.0F, 0F})
    void givenInvalidPrice_whenCreatingItem_thenThrowsException(float price) {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new ReceiptItem(price, VALID_NAME, VALID_AMOUNT));
        assertEquals(PRICE_ERROR_MESSAGE, exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "  "})
    void givenInvalidName_whenCreatingItem_thenThrowsException(String name) {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new ReceiptItem(VALID_PRICE, name, VALID_AMOUNT));
        assertEquals(NAME_ERROR_MESSAGE, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void givenInvalidAmount_whenCreatingItem_thenThrowsException(int amount) {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new ReceiptItem(VALID_PRICE, VALID_NAME, amount));
        assertEquals(AMOUNT_ERROR_MESSAGE, exception.getMessage());
    }
}