package ch.zhaw.it.pm2.receiptsplitter.model;

import ch.zhaw.it.pm2.receiptsplitter.model.Receipt.ReceiptErrorMessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//TODO: Add Mocking because all test are dependent on correct constructor initialization
public class ReceiptTest {

    private static final ReceiptItem VALID_RECEIPT_ITEM_ONE = new ReceiptItem(9.95F, "Burger", 1);
    private static final ReceiptItem VALID_RECEIPT_ITEM_TWO = new ReceiptItem(20F, "Extra Cheese", 20);
    private static final ReceiptItem VALID_RECEIPT_ITEM_THREE = new ReceiptItem(4.95F, "Fanta", 2);
    private Receipt receipt;

    @BeforeEach
    void setUp() {
        List<ReceiptItem> validReceiptItemList = new ArrayList<>();
        validReceiptItemList.add(VALID_RECEIPT_ITEM_ONE);
        validReceiptItemList.add(VALID_RECEIPT_ITEM_TWO);
        validReceiptItemList.add(VALID_RECEIPT_ITEM_THREE);
        receipt = new Receipt(validReceiptItemList);
    }

    @Test
    void constructor_ValidAttributes_ListCreated() {
        //TODO: Set Arrange and Act by including mocking and moving setup-method here
        //Assert
        assertReceiptItemAttributes(VALID_RECEIPT_ITEM_ONE, receipt.getReceiptItem(0));
        assertReceiptItemAttributes(VALID_RECEIPT_ITEM_TWO, receipt.getReceiptItem(1));
        assertReceiptItemAttributes(VALID_RECEIPT_ITEM_THREE, receipt.getReceiptItem(2));
    }

    @Test
    void getTotal_ValidAttributes_TotalGenerated(){
        //Arrange
        float localTotal = VALID_RECEIPT_ITEM_ONE.getPrice() + VALID_RECEIPT_ITEM_TWO.getPrice() + VALID_RECEIPT_ITEM_THREE.getPrice();

        //Act
        float generatedTotal = receipt.getReceiptTotal();

        //Assert
        assertEquals(localTotal, generatedTotal);
    }

    @Test
    void addReceiptItem_ValidAttributes_ReceiptItemAdded() {
        //Arrange
        ReceiptItem localReceiptItem = new ReceiptItem(4F, "Entry", 4);
        int receiptSizeBefore = receipt.getReceiptItems().size();

        //Act
        receipt.addReceiptItem(localReceiptItem);

        //Assert
        assertEquals(receiptSizeBefore + 1, receipt.getReceiptItems().size());
        assertReceiptItemAttributes(localReceiptItem, receipt.getReceiptItem(3));
    }

    @Test
    void updateReceiptItem_ValidAttributes_ReceiptItemUpdated() {
        //Arrange
        ReceiptItem localContact = new ReceiptItem(0.05F, "Trinkgeld", 1);
        int receiptSizeBefore = receipt.getReceiptItems().size();

        //Act
        receipt.updateReceiptItem(1, localContact);

        //Assert
        assertEquals(receiptSizeBefore, receipt.getReceiptItems().size());
        assertReceiptItemAttributes(localContact, receipt.getReceiptItem(1));
    }

    @Test
    void deleteReceiptItem_ValidAttributes_ReceiptItemDeleted() {
        //Arrange
        int receiptSizeBefore = receipt.getReceiptItems().size();
        receipt.deleteReceiptItem(0);

        //Act
        assertEquals(receiptSizeBefore - 1, receipt.getReceiptItems().size());

        //Assert
        assertReceiptItemAttributes(VALID_RECEIPT_ITEM_TWO, receipt.getReceiptItem(0));
        assertReceiptItemAttributes(VALID_RECEIPT_ITEM_THREE, receipt.getReceiptItem(1));
    }

    @Test
    void sortList_ValidAttributes_ListSorted(){
        receipt.sortByPriceLowestFirst();                                                                   //Act
        assertSortingOrder(VALID_RECEIPT_ITEM_THREE, VALID_RECEIPT_ITEM_ONE, VALID_RECEIPT_ITEM_TWO);       //Assert

        receipt.sortByPriceHighestFirst();                                                                  //Act
        assertSortingOrder(VALID_RECEIPT_ITEM_TWO, VALID_RECEIPT_ITEM_ONE, VALID_RECEIPT_ITEM_THREE);       //Assert

        receipt.sortByNameLowestFirst();                                                                    //Act
        assertSortingOrder(VALID_RECEIPT_ITEM_ONE, VALID_RECEIPT_ITEM_TWO, VALID_RECEIPT_ITEM_THREE);       //Assert

        receipt.sortByNameHighestFirst();                                                                   //Act
        assertSortingOrder(VALID_RECEIPT_ITEM_THREE, VALID_RECEIPT_ITEM_TWO, VALID_RECEIPT_ITEM_ONE);       //Assert

        receipt.sortByAmountLowestFirst();                                                                  //Act
        assertSortingOrder(VALID_RECEIPT_ITEM_ONE, VALID_RECEIPT_ITEM_THREE, VALID_RECEIPT_ITEM_TWO);       //Assert

        receipt.sortByAmountHighestFirst();                                                                 //Act
        assertSortingOrder(VALID_RECEIPT_ITEM_TWO, VALID_RECEIPT_ITEM_THREE, VALID_RECEIPT_ITEM_ONE);       //Assert
    }

    @ParameterizedTest
    @NullSource
    void setReceiptList_InvalidAttributes_ThrowsException(List<ReceiptItem> receiptItemList){
        //Arrange & Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> receipt.setReceiptItemList(receiptItemList));
        assertEquals(ReceiptErrorMessageType.LIST_NULL.toString(), exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {3, -1})
    void getReceiptItem_InvalidIndex_ThrowsException(int index){
        //Arrange & Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> receipt.getReceiptItem(index));
        assertEquals(ReceiptErrorMessageType.INDEX_NOT_PRESENT.toString(), exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void addReceiptList_InvalidAttributes_ThrowsException(ReceiptItem receiptItem) {
        //Arrange
        int receiptSizeBefore = receipt.getReceiptItems().size();

        //Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> receipt.addReceiptItem(receiptItem));
        assertEquals(ReceiptErrorMessageType.ITEM_NULL.toString(), exception.getMessage());
        assertEquals(receiptSizeBefore, receipt.getReceiptItems().size());
    }

    @ParameterizedTest
    @ValueSource(ints = {3, -1})
    void updateReceiptItem_InvalidIndex_ThrowsException(int index) {
        //Arrange
        ReceiptItem localContact = new ReceiptItem(0.05F, "Trinkgeld", 1);

        //Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> receipt.updateReceiptItem(index, localContact));
        assertEquals(ReceiptErrorMessageType.INDEX_NOT_PRESENT.toString(), exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {3, -1})
    void deleteReceiptItem_InvalidIndex_ThrowsException(int index) {
        //Arrange
        int receiptSizeBefore = receipt.getReceiptItems().size();

        //Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> receipt.deleteReceiptItem(index));
        assertEquals(ReceiptErrorMessageType.INDEX_NOT_PRESENT.toString(), exception.getMessage());
        assertEquals(receiptSizeBefore, receipt.getReceiptItems().size());
    }

    private void assertReceiptItemAttributes(ReceiptItem expected, ReceiptItem actual) {
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getAmount(), actual.getAmount());
    }

    private void assertSortingOrder(ReceiptItem firstItem, ReceiptItem secondItem, ReceiptItem thirdItem){
        assertEquals(firstItem, receipt.getReceiptItem(0));
        assertEquals(secondItem, receipt.getReceiptItem(1));
        assertEquals(thirdItem, receipt.getReceiptItem(2));
    }

}
