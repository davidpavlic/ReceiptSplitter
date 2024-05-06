package ch.zhaw.it.pm2.receiptsplitter.repository;
import ch.zhaw.it.pm2.receiptsplitter.model.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


class ReceiptProcessorTest {
    @Mock private Receipt receipt;
    @Mock private ReceiptItem item;

    private ReceiptProcessor receiptProcessor;
    private Contact validContact;
    private Contact invalidContact;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        receiptProcessor = new ReceiptProcessor();
        receiptProcessor.setReceipt(receipt);
        validContact = new Contact("Samuel", "Ammann", "samuel.ammann@swissmail.com");
        invalidContact = new Contact("Doesnt", "Exist", "invalid@nonexisting.com");
        item = new ReceiptItem(5.0f, "Tee", 1);

    }

    @Test
    void setReceipt_ValidInput_ReceiptAdded() {
        // Arrange
        Receipt newReceipt = mock(Receipt.class);

        // Act
        receiptProcessor.setReceipt(newReceipt);

        // Assert
        assertNotNull(receiptProcessor.getReceiptItems());
    }

    @Test
    void setReceipt_NullInput_ThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            receiptProcessor.setReceipt(null);
        });
    }

    @Test
    void splitReceiptItems_ValidReceiptItem_ItemsSplit() {
        // Arrange
        float price = 20F;
        int amount = 2;
        when(receipt.getReceiptItems()).thenReturn(Collections.singletonList(new ReceiptItem(price, "Pommes Frites", amount)));

        // Act
        List<ReceiptItem> splitItems = receiptProcessor.splitReceiptItems();

        // Assert
        assertEquals(amount, splitItems.size());
        assertEquals(price/amount, splitItems.get(0).getPrice());
    }

    @Test
    void splitReceiptItems_NoItems_EmptyList() {
        // Arrange
        when(receipt.getReceiptItems()).thenReturn(Collections.emptyList());

        // Act
        List<ReceiptItem> result = receiptProcessor.splitReceiptItems();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void createReceiptItem_ValidReceiptItem_NewItemAdded() {
        // Arrange
        List<ReceiptItem> items = receipt.getReceiptItems();
        when(receipt.getReceiptItems()).thenReturn(items);
        ReceiptItem newItem = new ReceiptItem(5.0f, "Kaffee", 1);

        // Act
        receiptProcessor.createReceiptItem(newItem);

        // Assert
        assertTrue(items.contains(newItem), "The new item should be added to the list.");
        assertEquals(1, items.size(), "There should be exactly one item on the list.");
    }

    @Test
    void updateReceiptItem_ValidReceiptItem_ItemUpdated() {
        // Arrange
        int newAmount = 3;
        List<ReceiptItem> items = receipt.getReceiptItems();
        when(receipt.getReceiptItems()).thenReturn(items);
        items.add(item);
        ReceiptItem newItem = new ReceiptItem(5.0f, "Tee", newAmount);

        // Act
        receiptProcessor.updateReceiptItemByName(newItem);

        // Assert
        assertTrue(items.contains(newItem), "The new item should still be on to the list.");
        assertEquals(1, items.size(), "There should be exactly one item on the list.");
        assertEquals(newAmount, items.get(0).getAmount());
    }

    @Test
    void createReceiptItem_NullInput_ThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> receiptProcessor.createReceiptItem(null));
    }

    @Test
    void deleteReceiptItem_ValidReceiptItem_itemDeleted() {
        // Arrange
        when(receipt.getReceiptItems()).thenReturn(new ArrayList<>(Collections.singletonList(item)));

        //Act
        receiptProcessor.deleteReceiptItem(item);
    }

    @Test
    void createContactReceiptItem_ValidContactAndReceiptItem_ContactReceiptItemCreated() {
        receiptProcessor.createContactReceiptItem(validContact, item);
        assertFalse(receiptProcessor.getContactReceiptItems().isEmpty());
        assertEquals(1, receiptProcessor.getContactReceiptItems().size());
        ContactReceiptItem createdItem = receiptProcessor.getContactReceiptItems().get(0);
        assertEquals("Tee", createdItem.getName());
        assertEquals(5.0, createdItem.getPrice());
        assertEquals(validContact, createdItem.getContact());
    }

    @Test
    void testCalculateDebtByPerson_validInput_correctDebt() {
        // Arrange
        ContactReceiptItem contactReceiptItem1 = new ContactReceiptItem(5.0f, "Tee", validContact);
        ContactReceiptItem contactReceiptItem2 = new ContactReceiptItem(3.0f, "Wasser", validContact);
        List<ContactReceiptItem> contactReceiptItems = Arrays.asList(contactReceiptItem1, contactReceiptItem2);
        receiptProcessor.setContactReceiptItems(contactReceiptItems);

        double expectedTotalDebt = contactReceiptItems.stream()
                .mapToDouble(ContactReceiptItem::getPrice)
                .sum();

        // Act
        double actualTotalDebt = receiptProcessor.calculateDebtByPerson(validContact);

        // Assert
        assertEquals(expectedTotalDebt, actualTotalDebt, 0.01, "The calculated debt should match the expected total debt.");
    }

    @Test
    void calculateDebtByPerson_EmptyList_ThrowException() {
        // Arrange
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
        ContactReceiptItem contactReceiptItem = new ContactReceiptItem(5.0f, "Tea", validContact);
        List<ContactReceiptItem> contactReceiptItems = Collections.singletonList(contactReceiptItem);
        receiptProcessor.setContactReceiptItems(contactReceiptItems);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> receiptProcessor.calculateDebtByPerson(invalidContact), "Should throw IllegalArgumentException because the contact does not exist.");
    }
}