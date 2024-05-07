package ch.zhaw.it.pm2.receiptsplitter.model;

import ch.zhaw.it.pm2.receiptsplitter.model.ReceiptItem.ReceiptItemErrorMessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void constructor_ValidAttributes_ItemCreated() {
        //Assert
        assertReceiptItemAttributes(VALID_PRICE, VALID_NAME, VALID_AMOUNT);
    }

    @Test
    void setPrice_ValidAttributes_PriceUpdated() {
        //Arrange
        float newPrice = 4.95F;

        //Act
        receiptItem.setPrice(newPrice);

        //Assert
        assertEquals(newPrice, receiptItem.getPrice());
    }

    @Test
    void setName_ValidAttributes_NameUpdated() {
        //Arrange
        String newName = "Vivi Cola";

        //Act
        receiptItem.setName(newName);

        //Assert
        assertEquals(newName, receiptItem.getName());
    }

    @Test
    void setAmount_ValidAttributes_AmountUpdated() {
        //Arrange
        int newAmount = 2;

        //Act
        receiptItem.setAmount(newAmount);

        //Assert
        assertEquals(newAmount, receiptItem.getAmount());
    }

    @ParameterizedTest
    @ValueSource(floats = {-1.0F, 0F})
    void setPrice_InvalidAttributes_ThrowsException(float price) {
        //Arrange & Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new ReceiptItem(price, VALID_NAME, VALID_AMOUNT));
        assertEquals(ReceiptItemErrorMessageType.PRICE_ZERO_OR_LOWER.toString(), exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "  "})
    void setName_InvalidAttributes_ThrowsException(String name) {
        //Arrange & Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new ReceiptItem(VALID_PRICE, name, VALID_AMOUNT));
        assertEquals(ReceiptItemErrorMessageType.NAME_EMPTY.toString(), exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void setAmount_InvalidAttributes_ThrowsException(int amount) {
        //Arrange & Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new ReceiptItem(VALID_PRICE, VALID_NAME, amount));
        assertEquals(ReceiptItemErrorMessageType.AMOUNT_ZERO_OR_LOWER.toString(), exception.getMessage());
    }

    @Test
    void roundPrice_PriceWithMoreThanTwoDecimalPlaces_RoundedPrice() {
        //Arrange
        float firstPrice = 9.999F;
        float secondPrice = 9.994F;
        float thirdPrice = 5.456666F;

        //Act
        float firstPriceRounded = ReceiptItem.roundPrice(firstPrice);
        float secondPriceRounded = ReceiptItem.roundPrice(secondPrice);
        float thirdPriceRounded = ReceiptItem.roundPrice(thirdPrice);

        //Assert
        assertEquals(10.0F, firstPriceRounded);
        assertEquals(9.99F, secondPriceRounded);
        assertEquals(5.46F, thirdPriceRounded);
    }

    private void assertReceiptItemAttributes(float price, String name, int amount) {
        assertEquals(price, receiptItem.getPrice());
        assertEquals(name, receiptItem.getName());
        assertEquals(amount, receiptItem.getAmount());
    }
}