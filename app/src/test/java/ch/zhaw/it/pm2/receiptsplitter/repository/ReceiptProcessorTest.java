package ch.zhaw.it.pm2.receiptsplitter.repository;

import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.model.ContactReceiptItem;
import ch.zhaw.it.pm2.receiptsplitter.model.Receipt;
import ch.zhaw.it.pm2.receiptsplitter.model.ReceiptItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceiptProcessorTest {

    @InjectMocks
    private ReceiptProcessor receiptProcessor;

    @Mock
    private Receipt receipt;

    @Mock
    private ReceiptItem receiptItem;

    List<ReceiptItem> receiptItems;

    @BeforeEach
    void setUp() {
        receiptItems = new ArrayList<>(Collections.singletonList(receiptItem));
        lenient().when(receipt.getReceiptItems()).thenReturn(receiptItems);
        generateMock(receiptItem, 5.0f, "Tee", 1);

        receiptProcessor.setReceipt(receipt);
    }

    private void generateMock(ReceiptItem mockItem, float price, String name, int amount) {
        lenient().when(mockItem.getPrice()).thenReturn(price);
        lenient().when(mockItem.getName()).thenReturn(name);
        lenient().when(mockItem.getAmount()).thenReturn(amount);
    }

    @Test
    void setReceipt_NullInput_ThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> receiptProcessor.setReceipt(null));
    }

    @Test
    void splitReceiptItems_ValidReceiptItem_ItemsSplit() {
        // Arrange
        float price = 20.0f;
        String name = "Pommes Frites";
        int amount = 2;

        ReceiptItem mockItem = mock(ReceiptItem.class);
        generateMock(mockItem, price, name, amount);

        // Act
        receiptProcessor.setReceipt(new Receipt(Collections.singletonList(mockItem)));

        List<ReceiptItem> splitItems = receiptProcessor.splitReceiptItems();

        // Assert
        assertEquals(amount, splitItems.size());
        assertEquals(price / amount, splitItems.get(0).getPrice());
    }

    @Test
    void splitReceiptItems_NoItems_EmptyList() {
        // Arrange
        receiptProcessor.setReceipt(new Receipt(Collections.emptyList()));

        // Act
        List<ReceiptItem> result = receiptProcessor.splitReceiptItems();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void addReceiptItem_ValidReceiptItem_NewItemAdded() {
        // Arrange
        ReceiptItem newItem = new ReceiptItem(5.0f, "Kaffee", 1);

        // Act
        receiptProcessor.addReceiptItem(newItem);

        // Assert
        verify(receipt).addReceiptItem(newItem);
    }

    @Test
    void updateReceiptItem_ValidReceiptItem_ItemUpdated() {
        // Arrange
        List<ReceiptItem> mockedReceiptItems = receiptItems;

        int sizeBefore = mockedReceiptItems.size();
        String oldName = "Tee";
        String newName = "Kaffee";
        int newAmount = 3;

        ReceiptItem newItem = mock(ReceiptItem.class);
        generateMock(newItem, 5.0f, newName, newAmount);

        when(receipt.getReceiptItemByName(oldName)).thenReturn(Optional.of(receiptItem));
        int expectedIndex = mockedReceiptItems.indexOf(receiptItem);

        // Act
        receiptProcessor.updateReceiptItemByName(oldName, newItem);

        // Assert
        verify(receipt).getReceiptItems();
        verify(receipt).getReceiptItemByName(oldName);
        verify(receipt).updateReceiptItem(expectedIndex, newItem);
        assertEquals(sizeBefore, receipt.getReceiptItems().size());
    }

    @Test
    void addReceiptItem_NullInput_ThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> receiptProcessor.addReceiptItem(null));
    }

    @Test
    void deleteReceiptItemByName_ExistingReceiptItem_IsDeleted() {
        // Arrange
        String name = receiptItems.getFirst().getName();
        when(receipt.getReceiptItems()).thenReturn(Collections.singletonList(receiptItem));
        when(receipt.getReceiptItemByName(name)).thenReturn(Optional.of(receiptItem));
        int expectedIndex = receiptItems.indexOf(receiptItem);

        //Act
        receiptProcessor.deleteReceiptItemByName(name);

        // Assert
        verify(receipt).deleteReceiptItem(expectedIndex);
    }

    @Test
    void createContactReceiptItem_ValidContactAndReceiptItem_ContactReceiptItemCreated() {
        // Arrange
        int sizeBefore = receiptProcessor.getContactReceiptItems().size();
        ReceiptItem item = receipt.getReceiptItems().getFirst();
        Contact validContact = getValidContact();

        // Act
        receiptProcessor.createContactReceiptItem(validContact, item);

        // Assert
        ContactReceiptItem createdItem = receiptProcessor.getContactReceiptItems().getFirst();

        assertEquals(sizeBefore + 1, receiptProcessor.getContactReceiptItems().size(), "The size of the list should be incremented by 1.");
        assertEquals(item.getName(), createdItem.getName(), "The name of the item should match the name of the created item.");
        assertEquals(item.getPrice(), createdItem.getPrice(), "The price of the item should match the price of the created item.");
        assertEquals(validContact, createdItem.getContact(), "The contact of the item should match the contact of the created item.");
    }

    @Test
    void calculateDebtByPerson_ValidInput_CalculationValid() {
        // Arrange
        Contact validContact = getValidContact();

        ContactReceiptItem contactReceiptItem1 = new ContactReceiptItem(5.0f, "Tee", validContact);
        ContactReceiptItem contactReceiptItem2 = new ContactReceiptItem(3.0f, "Wasser", validContact);
        List<ContactReceiptItem> contactReceiptItems = Arrays.asList(contactReceiptItem1, contactReceiptItem2);
        receiptProcessor.setContactReceiptItems(contactReceiptItems);

        float expectedTotalDebt = (float) contactReceiptItems.stream()
                .mapToDouble(ContactReceiptItem::getPrice)
                .sum();

        // Act
        float actualTotalDebt = receiptProcessor.calculateDebtByPerson(validContact);

        // Assert
        assertEquals(expectedTotalDebt, actualTotalDebt, 0.01, "The calculated debt should match the expected total debt.");
    }

    @Test
    void calculateDebtByPerson_EmptyList_ThrowException() {
        // Arrange
        Contact validContact = getValidContact();

        List<ContactReceiptItem> emptyContactReceiptItems = new ArrayList<>();
        receiptProcessor.setContactReceiptItems(emptyContactReceiptItems);

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> receiptProcessor.calculateDebtByPerson(validContact));

        // Assert
        assertTrue(thrown.getMessage().contains("The list is empty"), "Exception message should indicate that no items are associated with the contact.");
    }

    @Test
    void calculateDebtByPerson_NonExistingContact_ThrowException() {
        // Arrange
        Contact validContact = getValidContact();
        Contact invalidContact = getInvalidContact();

        ContactReceiptItem contactReceiptItem = new ContactReceiptItem(5.0f, "Tea", validContact);
        List<ContactReceiptItem> contactReceiptItems = Collections.singletonList(contactReceiptItem);
        receiptProcessor.setContactReceiptItems(contactReceiptItems);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> receiptProcessor.calculateDebtByPerson(invalidContact), "Should throw IllegalArgumentException because the contact does not exist.");
    }

    private static Contact getValidContact() {
        return new Contact("Samuel", "Ammann", "samuel.ammann@swissmail.com");
    }

    private static Contact getInvalidContact() {
        return new Contact("Doesnt", "Exist", "invalid@nonexisting.com");
    }
}