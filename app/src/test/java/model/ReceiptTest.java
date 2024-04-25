package model;

import ch.zhaw.it.pm2.receiptsplitter.model.Receipt;
import ch.zhaw.it.pm2.receiptsplitter.model.ReceiptItem;
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
//TODO: Enum for error messages?
public class ReceiptTest {

    private static final ReceiptItem VALID_RECEIPT_ITEM_ONE = new ReceiptItem(9.95F, "Burger", 1);
    private static final ReceiptItem VALID_RECEIPT_ITEM_TWO = new ReceiptItem(20F, "Extra Cheese", 20);
    private static final ReceiptItem VALID_RECEIPT_ITEM_THREE = new ReceiptItem(4.95F, "Fanta", 2);
    private List<ReceiptItem> validReceiptItemList;
    private Receipt receipt;

    private static final String LIST_ERROR_MESSAGE     = "ReceiptItemList must not be null.";
    private static final String ITEM_ERROR_MESSAGE     = "ReceiptItem must not be null.";
    private static final String INDEX_ERROR_MESSAGE    = "Index can not be present in list.";

    @BeforeEach
    void setUp() {
        validReceiptItemList = new ArrayList<>();
        validReceiptItemList.add(VALID_RECEIPT_ITEM_ONE);
        validReceiptItemList.add(VALID_RECEIPT_ITEM_TWO);
        validReceiptItemList.add(VALID_RECEIPT_ITEM_THREE);
        receipt = new Receipt(validReceiptItemList);
    }

    @Test
    void whenCreatingValidList_thenAllAttributesAreCorrect() {
        assertEquals(validReceiptItemList, receipt.getReceiptItemList());
        assertReceiptItemAttributes(VALID_RECEIPT_ITEM_ONE, receipt.getReceiptItem(0));
        assertReceiptItemAttributes(VALID_RECEIPT_ITEM_TWO, receipt.getReceiptItem(1));
        assertReceiptItemAttributes(VALID_RECEIPT_ITEM_THREE, receipt.getReceiptItem(2));
    }

    @Test
    void whenGeneratingTotal_thenTotalIsCorrect(){
        float localTotal = VALID_RECEIPT_ITEM_ONE.getPrice() + VALID_RECEIPT_ITEM_TWO.getPrice() + VALID_RECEIPT_ITEM_THREE.getPrice();
        assertEquals(localTotal, receipt.getReceiptTotal());
    }

    @Test
    void whenAddingItem_thenAllAttributesAreCorrect() {
        ReceiptItem localContact = new ReceiptItem(4F, "Entry", 4);
        int receiptSizeBefore = receipt.getReceiptItemList().size();

        receipt.addReceiptItem(localContact);
        assertEquals(receiptSizeBefore + 1, receipt.getReceiptItemList().size());
        assertReceiptItemAttributes(localContact, receipt.getReceiptItem(3));
    }

    @Test
    void whenUpdatingItem_thenAllAttributesAreUpdated() {
        ReceiptItem localContact = new ReceiptItem(0.05F, "Trinkgeld", 1);
        int receiptSizeBefore = receipt.getReceiptItemList().size();

        receipt.updateReceiptItem(1, localContact);
        assertEquals(receiptSizeBefore, receipt.getReceiptItemList().size());
        assertReceiptItemAttributes(localContact, receipt.getReceiptItem(1));
    }

    @Test
    void whenDeletingItem_thenListSizeIsReduced() {
        int receiptSizeBefore = receipt.getReceiptItemList().size();
        receipt.deleteReceiptItem(0);
        assertEquals(receiptSizeBefore - 1, receipt.getReceiptItemList().size());
        assertReceiptItemAttributes(VALID_RECEIPT_ITEM_TWO, receipt.getReceiptItem(0));
        assertReceiptItemAttributes(VALID_RECEIPT_ITEM_THREE, receipt.getReceiptItem(1));
    }

    @Test
    void whenSortingList_thenAllItemsAreSorted(){
        receipt.sortByPriceLowestFirst();
        assertSortingOrder(VALID_RECEIPT_ITEM_THREE, VALID_RECEIPT_ITEM_ONE, VALID_RECEIPT_ITEM_TWO);

        receipt.sortByPriceHighestFirst();
        assertSortingOrder(VALID_RECEIPT_ITEM_TWO, VALID_RECEIPT_ITEM_ONE, VALID_RECEIPT_ITEM_THREE);

        receipt.sortByNameLowestFirst();
        assertSortingOrder(VALID_RECEIPT_ITEM_ONE, VALID_RECEIPT_ITEM_TWO, VALID_RECEIPT_ITEM_THREE);

        receipt.sortByNameHighestFirst();
        assertSortingOrder(VALID_RECEIPT_ITEM_THREE, VALID_RECEIPT_ITEM_TWO, VALID_RECEIPT_ITEM_ONE);

        receipt.sortByAmountLowestFirst();
        assertSortingOrder(VALID_RECEIPT_ITEM_ONE, VALID_RECEIPT_ITEM_THREE, VALID_RECEIPT_ITEM_TWO);

        receipt.sortByAmountHighestFirst();
        assertSortingOrder(VALID_RECEIPT_ITEM_TWO, VALID_RECEIPT_ITEM_THREE, VALID_RECEIPT_ITEM_ONE);
    }

    @ParameterizedTest
    @NullSource
    void givenInvalidList_whenSettingList_thenThrowsException(List<ReceiptItem> receiptItemList){
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> receipt.setReceiptItemList(receiptItemList));
        assertEquals(exception.getMessage(), LIST_ERROR_MESSAGE);
    }

    @ParameterizedTest
    @ValueSource(ints = {3, -1})
    void givenInvalidIndex_whenGettingItem_thenThrowsException(int index){
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> receipt.getReceiptItem(index));
        assertEquals(exception.getMessage(), INDEX_ERROR_MESSAGE);
    }

    @ParameterizedTest
    @NullSource
    void givenInvalidItem_whenAddingItem_thenThrowsException(ReceiptItem receiptItem) {
        int receiptSizeBefore = receipt.getReceiptItemList().size();

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> receipt.addReceiptItem(receiptItem));
        assertEquals(exception.getMessage(), ITEM_ERROR_MESSAGE);
        assertEquals(receiptSizeBefore, receipt.getReceiptItemList().size());
    }

    @ParameterizedTest
    @ValueSource(ints = {3, -1})
    void givenInvalidIndex_whenUpdatingItem_thenThrowsException(int index) {
        ReceiptItem localContact = new ReceiptItem(0.05F, "Trinkgeld", 1);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> receipt.updateReceiptItem(index, localContact));
        assertEquals(exception.getMessage(), INDEX_ERROR_MESSAGE);
    }

    @ParameterizedTest
    @ValueSource(ints = {3, -1})
    void givenInvalidIndex_whenDeletingItem_thenThrowsException(int index) {
        int receiptSizeBefore = receipt.getReceiptItemList().size();
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> receipt.deleteReceiptItem(index));
        assertEquals(exception.getMessage(), INDEX_ERROR_MESSAGE);
        assertEquals(receiptSizeBefore, receipt.getReceiptItemList().size());
    }

    private void assertReceiptItemAttributes(ReceiptItem expected, ReceiptItem actual) {
        assertEquals(expected, actual);
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
