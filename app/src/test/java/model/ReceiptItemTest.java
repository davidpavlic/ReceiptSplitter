package model;

import ch.zhaw.it.pm2.receiptsplitter.model.ReceiptItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//TODO: Add Mocking because all test are dependent on correct constructor initialization
public class ReceiptItemTest {

    private static final float VALID_PRICE = 9.95F;
    private static final String VALID_NAME = "Coca-Cola";
    private static final int VALID_AMOUNT = 3;
    private ReceiptItem receiptItem;

    @BeforeEach
    void setUp() {
        receiptItem = new ReceiptItem(VALID_PRICE, VALID_NAME, VALID_AMOUNT);
    }

    @Test
    void createValidItem() {
        assertEquals(VALID_PRICE, receiptItem.getPrice());
        assertEquals(VALID_NAME, receiptItem.getName());
        assertEquals(VALID_AMOUNT, receiptItem.getAmount());
    }

    @Test
    void updateExistingItem() {
        float newPrice = 4.95F;
        String newName = "Vivi Cola";
        int newAmount = 2;

        receiptItem.setPrice(newPrice);
        receiptItem.setName(newName);
        receiptItem.setAmount(newAmount);

        assertEquals(newPrice, receiptItem.getPrice());
        assertEquals(newName, receiptItem.getName());
        assertEquals(newAmount, receiptItem.getAmount());
    }

    @Test
    void invalidPriceTest() {
        Exception exception;

        exception = assertThrows(IllegalArgumentException.class, () -> new ReceiptItem(-9.95F, VALID_NAME, VALID_AMOUNT));
        assertEquals("Price must not be zero or lower.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> new ReceiptItem(0, VALID_NAME, VALID_AMOUNT));
        assertEquals("Price must not be zero or lower.", exception.getMessage());
    }

    @Test
    void invalidNameTest() {
        Exception exception;

        exception= assertThrows(IllegalArgumentException.class, () -> new ReceiptItem(VALID_PRICE, "", VALID_AMOUNT));
        assertEquals("Name must not be empty.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> new ReceiptItem(VALID_PRICE, null, VALID_AMOUNT));
        assertEquals("Name must not be empty.", exception.getMessage());
    }

    @Test
    void invalidAmountTest() {
        Exception exception;

        exception = assertThrows(IllegalArgumentException.class, () -> new ReceiptItem(VALID_PRICE, VALID_NAME, -1));
        assertEquals("Amount must not be zero or lower.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> new ReceiptItem(VALID_PRICE, VALID_NAME, 0));
        assertEquals("Amount must not be zero or lower.", exception.getMessage());
    }
}