package ch.zhaw.it.pm2.receiptsplitter.repository;
import ch.zhaw.it.pm2.receiptsplitter.model.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


class ReceiptProcessorTest {
    @Mock private Receipt receipt;
    @Mock private ReceiptItem item;
    @Mock private Contact contact;

    private ReceiptProcessor receiptProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        receiptProcessor = Mockito.spy(new ReceiptProcessor());
        receiptProcessor.addReceipt(receipt);
        when(contact.getEmail()).thenReturn("samuel.ammann@swissmail.com");
        doReturn(true).when(receiptProcessor).doesContactExist(any(Contact.class));
        item = new ReceiptItem(15.0f, "Tea", 1);
    }

    // Test for addReceipt method
    @Test
    void testAddReceipt_validInput_receiptAdded() {
        Receipt newReceipt = mock(Receipt.class);
        assertDoesNotThrow(() -> receiptProcessor.addReceipt(newReceipt));
        assertNotNull(receiptProcessor.getReceiptItems());
    }

    @Test
    void testAddReceipt_nullInput_throwException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> receiptProcessor.addReceipt(null));
        assertEquals("Receipt cannot be null.", exception.getMessage());
    }

    // Test for splitReceiptItems method
    @Test
    void testSplitReceiptItems_validInput_itemsSplitted() {
        when(receipt.getReceiptItems()).thenReturn(Collections.singletonList(new ReceiptItem(20.0F, "Pommes Frites", 2)));
        List<ReceiptItem> splittedItems = receiptProcessor.splitReceiptItems();
        assertEquals(2, splittedItems.size());
        assertEquals(10.0, splittedItems.get(0).getPrice());
    }

    @Test
    void testSplitReceiptItems_noItems_emptyList() {
        when(receipt.getReceiptItems()).thenReturn(Collections.emptyList());
        assertTrue(receiptProcessor.splitReceiptItems().isEmpty());
    }

    // Test for createOrUpdateReceiptItem method
    @Test
    void testCreateOrUpdateReceiptItem_validInput_addNewItem() {
        when(receipt.getReceiptItems()).thenReturn(new ArrayList<>());
        assertDoesNotThrow(() -> receiptProcessor.createOrUpdateReceiptItem(item));
        verify(receipt).getReceiptItems();
    }

    @Test
    void testCreateOrUpdateReceiptItem_nullInput_throwException() {
        Exception exception = assertThrows(Exception.class, () -> receiptProcessor.createOrUpdateReceiptItem(null));
        assertEquals("Receipt item cannot be null.", exception.getMessage());
    }

    // Test for deleteReceiptItem method
    @Test
    void testDeleteReceiptItem_validInput_itemDeleted() {
        when(receipt.getReceiptItems()).thenReturn(new ArrayList<>(Collections.singletonList(item)));
        assertDoesNotThrow(() -> receiptProcessor.deleteReceiptItem(item));
        verify(receipt).getReceiptItems();
    }

    // Test for createContactReceiptItem method
    @Test
    void testCreateContactReceiptItem_validInput_contactReceiptItemCreated() {
        receiptProcessor.createContactReceiptItem(contact, item);
        assertFalse(receiptProcessor.getContactReceiptItems().isEmpty());
        assertEquals(1, receiptProcessor.getContactReceiptItems().size());
        ContactReceiptItem createdItem = receiptProcessor.getContactReceiptItems().get(0);
        assertEquals("Tea", createdItem.getName());
        assertEquals(15.0, createdItem.getPrice());
        assertEquals(contact, createdItem.getContact());
    }

    @Test
    void testCreateContactReceiptItem_invalidContact_throwException() {
        Contact invalidContact = new Contact("John", "Doe", "invalid@example.com");
        when(receiptProcessor.doesContactExist(invalidContact)).thenReturn(false);
        ReceiptItem receiptItem = new ReceiptItem(10.0f, "Coffee", 2);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> receiptProcessor.createContactReceiptItem(invalidContact, receiptItem));
        assertEquals("Contact does not exist.", exception.getMessage());
    }

    // Multiple tests for calculateDebtByPerson
    @Test
    void testCalculateDebtByPerson_validInput_correctDebt() {
        when(receipt.getReceiptItems()).thenReturn(Collections.singletonList(item));
        when(contact.getEmail()).thenReturn("samuel.ammann@swissmail.com");

        ContactReceiptItem cri = new ContactReceiptItem(10.0F, "Test Item", contact);
        receiptProcessor.setContactReceiptItems(Collections.singletonList(cri));

        assertEquals(10.0, receiptProcessor.calculateDebtByPerson(contact));
    }

    @Test
    void testCalculateDebtByPerson_emptyList_throwException() {
        when(receiptProcessor.doesContactExist(contact)).thenReturn(false);  // Ensure this contact is considered non-existent
        Exception exception = assertThrows(IllegalArgumentException.class, () -> receiptProcessor.calculateDebtByPerson(contact));
        assertEquals("The list is empty.", exception.getMessage());
    }
}