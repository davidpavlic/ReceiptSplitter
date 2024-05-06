package ch.zhaw.it.pm2.receiptsplitter.repository;

import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.model.ContactReceiptItem;
import ch.zhaw.it.pm2.receiptsplitter.model.Receipt;
import ch.zhaw.it.pm2.receiptsplitter.model.ReceiptItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ReceiptProcessorTest {
    private ReceiptProcessor receiptProcessor;
    private Receipt receipt;

    @BeforeEach
    void setUp() {
        List<ReceiptItem> receiptItems = new ArrayList<>() {{
            add(new ReceiptItem(5.0f, "Tee", 1));
        }};

        receipt = new Receipt(receiptItems);
        receiptProcessor = new ReceiptProcessor();
        receiptProcessor.setReceipt(receipt);
    }

    @Test
    void setReceipt_NullInput_ThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> receiptProcessor.setReceipt(null));
    }

    @Test
    void splitReceiptItems_ValidReceiptItem_ItemsSplit() {
        // Arrange
        float price = 20F;
        int amount = 2;
        ReceiptItem receiptItem = new ReceiptItem(price, "Pommes Frites", amount);
        receiptProcessor.setReceipt(new Receipt(Collections.singletonList(receiptItem)));

        // Act
        List<ReceiptItem> splitItems = receiptProcessor.splitReceiptItems();

        // Assert
        assertEquals(amount, splitItems.size());
        assertEquals(price / amount, splitItems.getFirst().getPrice());
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
        int sizeBefore = receipt.getReceiptItems().size();
        List<ReceiptItem> items = receipt.getReceiptItems();
        ReceiptItem newItem = new ReceiptItem(5.0f, "Kaffee", 1);

        // Act
        receiptProcessor.addReceiptItem(newItem);

        // Assert
        assertTrue(items.contains(newItem), "The new item should be added to the list.");
        assertEquals(sizeBefore + 1, items.size(), "The size of the list should be incremented by 1.");
    }

    @Test
    void updateReceiptItem_ValidReceiptItem_ItemUpdated() {
        // Arrange
        int sizeBefore = receipt.getReceiptItems().size();
        String oldName = receipt.getReceiptItems().getFirst().getName();
        String newName = "Kaffee";
        int newAmount = 3;

        ReceiptItem newItem = new ReceiptItem(5.0f, newName, newAmount);

        // Act
        receiptProcessor.updateReceiptItemByName(oldName, newItem);

        // Assert
        List<ReceiptItem> receiptItems = receipt.getReceiptItems();
        ReceiptItem actualReceiptItem = receiptItems.getLast();

        assertEquals(newName, actualReceiptItem.getName(), "The item name is expected to be updated.");
        assertEquals(newAmount, actualReceiptItem.getAmount(), "The item amount is expected to be updated.");
        assertEquals(sizeBefore, receiptItems.size(), "The size of the list should remain the same.");
    }

    @Test
    void addReceiptItem_NullInput_ThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> receiptProcessor.addReceiptItem(null));
    }

    @Test
    void deleteReceiptItemByName_ExistingReceiptItem_IsDeleted() {
        // Arrange
        ReceiptItem existingItem = receipt.getReceiptItems().getFirst();

        //Act
        boolean isSuccessful = receiptProcessor.deleteReceiptItemByName(existingItem.getName());

        // Assert
        assertTrue(isSuccessful, "The deletion of the item should be successful.");
        assertFalse(receipt.getReceiptItems().contains(existingItem), "The item should be removed from the list.");
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