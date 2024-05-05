package ch.zhaw.it.pm2.receiptsplitter.repository;
import ch.zhaw.it.pm2.receiptsplitter.model.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Manages receipt-related operations including item management and allocation to contacts.
 */
public class ReceiptProcessor {
    private Receipt receipt;
    private List<ContactReceiptItem> contactReceiptItems;

    /**
     * Constructs a new ReceiptProcessor instance with an empty list of contact-receipt-item associations.
     */
    public ReceiptProcessor() {
        this.contactReceiptItems = new ArrayList<>();
    }

    /**
     * Adds or replaces the current receipt. If a receipt is already present, it will be replaced.
     *
     * @param receipt the new receipt to be managed
     * @throws IllegalArgumentException if the provided receipt is null
     */
    public void setReceipt(Receipt receipt){
        if (receipt == null) {
            throw new IllegalArgumentException("Receipt cannot be null");
        }
        this.receipt = receipt;
    }


    /**
     * Splits bulk items in the receipt into individual items to handle scenarios where items bought together
     * need to be billed separately.
     *
     * @return a list of individually split ReceiptItems
     */
    public List<ReceiptItem> splitReceiptItems(){
        List< ReceiptItem> splitReceiptItems = new ArrayList<>();
        List<ReceiptItem> receiptItems = getReceiptItems();

        for (ReceiptItem receiptItem : receiptItems) {
            List<ReceiptItem> splitReceiptItem = splitIntoIndividualReceiptItems(receiptItem);
            splitReceiptItems.addAll(splitReceiptItem);
        }
        return splitReceiptItems;
    }


    /**
     * Splits a single ReceiptItem into multiple items based on its quantity, setting each item's quantity to 1.
     *
     * @param receiptItem the ReceiptItem to be split
     * @return a list of individual ReceiptItems
     * @throws IllegalArgumentException if the receipt item's amount is less than 1
     */
    private List<ReceiptItem> splitIntoIndividualReceiptItems(ReceiptItem receiptItem) throws IllegalArgumentException{
        List<ReceiptItem> splitReceiptItem = new ArrayList<>();
        int amount = receiptItem.getAmount();

        for (int counter = 0; counter < amount; counter++) {
            splitReceiptItem.add(new ReceiptItem(receiptItem.getPrice() / amount, receiptItem.getName(), 1));
        }

        return  splitReceiptItem;
    }


    /**
     * Replaces or adds a ReceiptItem to the list of receipt items.
     * If the item exists, it is updated; otherwise, it is added.
     *
     * @param receiptItem the ReceiptItem to be updated or added
     * @throws IllegalArgumentException if the receiptItem is null
     */
    public void createOrUpdateReceiptItem(ReceiptItem receiptItem) {
        if (receiptItem == null) {
            throw new IllegalArgumentException("Receipt item cannot be null.");
        }
        List<ReceiptItem> receiptItems = receipt.getReceiptItems();
        int index = receiptItems.indexOf(receiptItem);
        if (index != -1) {
            receiptItems.set(index, receiptItem);
        } else {
            receiptItems.add(receiptItem);
        }
    }


    /**
     * Removes a specified ReceiptItem from the list.
     *
     * @param receiptItem the ReceiptItem to be removed
     * @throws IllegalArgumentException if the receiptItem is null
     */
    public void deleteReceiptItem(ReceiptItem receiptItem)  {
        if (receiptItem == null) {
            throw new IllegalArgumentException("Receipt item cannot be null.");
        }
        List<ReceiptItem> receiptItems = receipt.getReceiptItems();
        receiptItems.remove(receiptItem);
    }


    /**
     * Associates a contact with a ReceiptItem.
     *
     * @param contact the contact to be associated
     * @param receiptItem the ReceiptItem to associate
     * @throws IllegalArgumentException if the contact does not exist or receiptItem is null
     */
    public void  createContactReceiptItem(Contact contact, ReceiptItem receiptItem) {
        if (!doesContactExist(contact)) {
            throw new IllegalArgumentException("Contact does not exist.");
        }
        contactReceiptItems.add(new ContactReceiptItem(receiptItem.getPrice(), receiptItem.getName(),  contact));
    }


    /**
     * Retrieves all ContactReceiptItems associated with a given contact's email.
     *
     * @param email the email of the contact
     * @return a list of ContactReceiptItems linked to the specified email
     */
    @NotNull
    public List<ContactReceiptItem> getContactItemsByContact(String email) {
        return contactReceiptItems.stream()
                .filter(contactReceiptItem -> contactReceiptItem.getContact().getEmail().equals(email))
                .collect(Collectors.toList());
    }


    /**
     * Calculates the total amount owed by a specific contact based on associated receipt items.
     *
     * @param contact the contact for which to calculate total debt
     * @return the total amount owed
     * @throws IllegalArgumentException if no items are associated with the contact or the contact does not exist
     */
    public double calculateDebtByPerson(Contact contact)  {
        List<ContactReceiptItem> specificContactItemsList = getContactItemsByContact(contact.getEmail());
        if (specificContactItemsList.isEmpty()) {
            throw new IllegalArgumentException("The list is empty.");
        }

        double total = 0;

        for (ContactReceiptItem contactReceiptItem : specificContactItemsList) {
            if (!doesContactExist(contactReceiptItem.getContact())) {
                throw new IllegalArgumentException("Contact does not exist.");
            }
            total += contactReceiptItem.getPrice();
        }
        return total;
    }



    /**
     * Retrieves the list of receipt items in an unmodifiable format to prevent external modifications.
     *
     * @return an unmodifiable list of receipt items
     */
    public List<ReceiptItem> getReceiptItems() {
        return Collections.unmodifiableList(receipt.getReceiptItems());
    }

    /**
     * Returns the current list of all associated contact-receipt items.
     *
     * @return a list of ContactReceiptItems
     */
    public List<ContactReceiptItem> getContactReceiptItems(){
        return contactReceiptItems;
    }


    /**
     * Updates the internal list of contact-receipt items with a new list.
     *
     * @param contactReceiptItems the new list of ContactReceiptItem to replace the existing one
     */
    public  void setContactReceiptItems(List<ContactReceiptItem> contactReceiptItems){
        this.contactReceiptItems = contactReceiptItems;
    }

    /**
     * Checks if a contact exists in the contact-receipt-item list.
     *
     * @param contact the contact to verify
     * @return true if the contact exists, false otherwise
     */
    protected boolean doesContactExist(Contact contact) {
        if (contact == null) {
            return false;
        }
        return contactReceiptItems.stream()
                .anyMatch(item -> item.getContact().equals(contact));
    }
}


