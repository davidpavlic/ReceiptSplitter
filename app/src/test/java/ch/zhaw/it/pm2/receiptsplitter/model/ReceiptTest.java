package ch.zhaw.it.pm2.receiptsplitter.model;

import ch.zhaw.it.pm2.receiptsplitter.model.Receipt.ReceiptErrorMessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReceiptTest {

    @Mock
    private ReceiptItem mockReceiptItemOne;
    @Mock
    private ReceiptItem mockReceiptItemTwo;
    @Mock
    private ReceiptItem mockReceiptItemThree;

    private Receipt receipt;

    @BeforeEach
    void setUp() {
        generateMock(mockReceiptItemOne, 9.95F, "Burger", 1);
        generateMock(mockReceiptItemTwo, 20F, "Extra Cheese", 20);
        generateMock(mockReceiptItemThree, 4.95F, "Fanta", 2);

        List<ReceiptItem> validReceiptItemList = new ArrayList<>();
        validReceiptItemList.add(mockReceiptItemOne);
        validReceiptItemList.add(mockReceiptItemTwo);
        validReceiptItemList.add(mockReceiptItemThree);

        receipt = new Receipt(validReceiptItemList);
    }

    private void generateMock(ReceiptItem mockItem, float price, String name, int amount){
        lenient().when(mockItem.getPrice()).thenReturn(price);
        lenient().when(mockItem.getName()).thenReturn(name);
        lenient().when(mockItem.getAmount()).thenReturn(amount);
    }

    @Test
    void constructor_ValidAttributes_ListCreated() {
        //Assert
        assertReceiptItemAttributes(mockReceiptItemOne, receipt.getReceiptItem(0));
        assertReceiptItemAttributes(mockReceiptItemTwo, receipt.getReceiptItem(1));
        assertReceiptItemAttributes(mockReceiptItemThree, receipt.getReceiptItem(2));
    }

    @Test
    void getTotal_ValidAttributes_TotalGenerated(){
        //Arrange
        float localTotal = mockReceiptItemOne.getPrice() + mockReceiptItemTwo.getPrice() + mockReceiptItemThree.getPrice();

        //Act
        float generatedTotal = receipt.getReceiptTotal();

        //Assert
        assertEquals(localTotal, generatedTotal);
    }

    @Test
    void addReceiptItem_ValidAttributes_ReceiptItemAdded() {
        //Arrange
        ReceiptItem mockNewReceiptItem = mock(ReceiptItem.class);
        generateMock(mockNewReceiptItem, 4F, "Entry", 4);

        int receiptSizeBefore = receipt.getReceiptItems().size();

        //Act
        receipt.addReceiptItem(mockNewReceiptItem);

        //Assert
        assertEquals(receiptSizeBefore + 1, receipt.getReceiptItems().size());
        assertReceiptItemAttributes(mockNewReceiptItem, receipt.getReceiptItem(receiptSizeBefore));
    }

    @Test
    void updateReceiptItem_ValidAttributes_ReceiptItemUpdated() {
        //Arrange
        int index = 1;
        ReceiptItem mockUpdatedReceiptItem = mock(ReceiptItem.class);
        generateMock(mockUpdatedReceiptItem, 0.05F, "Trinkgeld", 1);

        int receiptSizeBefore = receipt.getReceiptItems().size();

        //Act
        receipt.updateReceiptItem(index, mockUpdatedReceiptItem);

        //Assert
        assertEquals(receiptSizeBefore, receipt.getReceiptItems().size());
        verify(mockReceiptItemTwo).setPrice(mockUpdatedReceiptItem.getPrice());
        verify(mockReceiptItemTwo).setName(mockUpdatedReceiptItem.getName());
        verify(mockReceiptItemTwo).setAmount(mockUpdatedReceiptItem.getAmount());
    }

    @Test
    void deleteReceiptItem_ValidIndex_ReceiptItemDeleted() {
        //Arrange
        int index = 0;
        int receiptSizeBefore = receipt.getReceiptItems().size();

        //Act
        receipt.deleteReceiptItem(index);

        //Assert
        assertEquals(receiptSizeBefore - 1, receipt.getReceiptItems().size());
        assertReceiptItemAttributes(mockReceiptItemTwo, receipt.getReceiptItem(index));
        assertReceiptItemAttributes(mockReceiptItemThree, receipt.getReceiptItem(1));
    }

    @ParameterizedTest
    @NullSource
    void setReceiptList_InvalidAttributes_ThrowsException(List<ReceiptItem> receiptItemList){
        //Arrange & Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> receipt.setReceiptItems(receiptItemList));
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
        ReceiptItem mockUpdatedReceiptItem = mock(ReceiptItem.class);
        generateMock(mockUpdatedReceiptItem, 0.05F, "Trinkgeld", 1);

        //Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> receipt.updateReceiptItem(index, mockUpdatedReceiptItem));
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
}
