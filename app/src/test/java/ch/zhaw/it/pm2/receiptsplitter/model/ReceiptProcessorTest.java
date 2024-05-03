package ch.zhaw.it.pm2.receiptsplitter.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReceiptProcessorTest {
    private Receipt receipt;
    private ReceiptProcessor receiptProcessor;
    private ReceiptItem receiptItem1, receiptItem2;
    private ContactReceiptItem contactItem1, contactItem2, contactItem3;
    private Contact contact;

    @BeforeEach
    public void setUp() {
        receipt = mock(Receipt.class);
        receiptProcessor = new ReceiptProcessor(receipt);
        receiptItem1 = new ReceiptItem(10.0f, "Grüntee", 2);
        receiptItem2 = new ReceiptItem(5.0f, "Mini-Pic", 3);

        contact = new Contact("Samuel", "Ammann", "saemi.ammann@schwiz.com");
        contactItem1 = new ContactReceiptItem(receiptItem1.getPrice(), receiptItem1.getName(), contact);
        contactItem2 = new ContactReceiptItem(receiptItem2.getPrice(), receiptItem2.getName(), contact);
        contactItem3 = new ContactReceiptItem(receiptItem2.getPrice(), receiptItem2.getName(), contact);
    }


    @Test
    public void testGetReceiptItems_ValidAttributes_ReturnsCorrectItems() {
        when(receipt.getReceiptItemList()).thenReturn(Arrays.asList(receiptItem1, receiptItem2));
        List<ReceiptItem> items = receiptProcessor.getReceiptItems();
        assertNotNull(items);
        assertEquals(2, items.size());
        assertTrue(items.contains(receiptItem1), "List should contain Grüntee");
        assertTrue(items.contains(receiptItem2), "List should contain Mini-Pic");
        verify(receipt).getReceiptItemList();
    }

    @Test
    public void testCreateContactReceiptItemList_ValidAttributes_AssociatesItemsWithContact() {
        when(receipt.getReceiptItemList()).thenReturn(Arrays.asList(receiptItem1, receiptItem2));
        receiptProcessor.createContactReceiptItemList(contact);
        verify(receipt).getReceiptItemList();

        List<ContactReceiptItem> contactItems = receiptProcessor.getContactItemsByContact(contact);
        assertEquals(2, contactItems.size());
        assertEquals(contact, contactItems.get(0).getContact());
        assertEquals(contact, contactItems.get(1).getContact());
    }

    @Test
    public void testCalculateDebtByPerson_ValidAttributes_CalculatesCorrectTotal() {
        List<ContactReceiptItem> contactItems = Arrays.asList(contactItem1, contactItem2, contactItem3);
        receiptProcessor.setContactReceiptItems(contactItems);

        double total = receiptProcessor.calculateDebtByPerson(contactItems);
        assertEquals(20.0, total, 0.001); // Check if the total calculation is correct
    }

    @Test
    public void testCalculateDebtByPerson_emptyList_ThrowsIllegalArgumentException() {
        List<ContactReceiptItem> emptyList = new ArrayList<>();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            receiptProcessor.calculateDebtByPerson(emptyList);
        });
        assertEquals("ContactItems list cannot be empty.", exception.getMessage());
    }

    /*
    @Test
    public void testCalculateDebtByPerson_contactItemsWithNegativeAmount_ThrowsIllegalArgumentException() {
        ReceiptItem receiptItemNegative = new ReceiptItem(-5.0f, "Geschenk", 3);
        ContactReceiptItem contactItemNegative = new ContactReceiptItem(receiptItemNegative.getPrice(), receiptItemNegative.getName(), contact);
        List<ContactReceiptItem> contactItems = Arrays.asList(contactItem1, contactItemNegative);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            receiptProcessor.calculateDebtByPerson(contactItems);
        });
        assertEquals("Price must not be zero or lower." , exception.getMessage());
    }
     */
}


