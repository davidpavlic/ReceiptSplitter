package ch.zhaw.it.pm2.receiptsplitter.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ReceiptProcessor class handles the operations related to processing a receipt,
 * including managing receipt items and allocating them to contacts.
 */
public class ReceiptProcessor {
    private final Receipt receipt;
    private List<ContactReceiptItem> contactReceiptItems;

    public ReceiptProcessor(Receipt receipt) {
        ModelParamValidator.throwIfElementIsNull(receipt, "Receipt cannot be null.");
        this.receipt = receipt;
        this.contactReceiptItems = new ArrayList<>();
    }

    /**
     * Method to get a list of all receipt items along with their prices.
     * @return List of ReceiptItem
     */
    public List<ReceiptItem> getReceiptItems() {
        return new ArrayList<>(receipt.getReceiptItemList());
    }

    public List<ContactReceiptItem> getContactReceiptItems(){
        return contactReceiptItems;
    }

    public  void setContactReceiptItems(List<ContactReceiptItem> contactReceiptItems){
        this.contactReceiptItems = contactReceiptItems;
    }


    /**
     * Changes list of ReceiptItemOCR objects to ReceiptItems..
     * @param ocrItems List of ReceiptItemOCR representing OCR data for receipt items.
     */
    /*
    public void createReceiptItemsFromOCR(List<ReceiptItemOCR> ocrItems) {
        ModelParamValidator.throwIfElementIsNull(ocrItems, "OCR items list cannot be null.");
        for (ReceiptItemOCR ocrItem : ocrItems) {
            ReceiptItem newItem = new ReceiptItem(
                    ocrItem.getPrice(),      // Rounded and extracted price
                    ocrItem.getName(),       // Item description
                    ocrItem.getAmount()      // Quantity
            );
            this.receipt.addReceiptItem(newItem);  // Add the new item to the Receipt
        }
    }
    */


    private List<ReceiptItem> splitReceiptItems(){
        List<ReceiptItem> receiptItems = getReceiptItems();

        for (ReceiptItem receiptItem : receiptItems) {
            addIndividualReceiptItem(receiptItem);
        }
        return receiptItems;
    }

    /**
     * Adds a ReceiptItem to the list. If the amount is more than one, it adds
     * multiple instances of the ReceiptItem to the list according to the amount.
     *
     * @param receiptItem The ReceiptItem to be added.
     */
    private void addIndividualReceiptItem(ReceiptItem receiptItem) {
        if (receiptItem == null) {
            throw new IllegalArgumentException("ReceiptItem cannot be null.");
        }

        int amount = receiptItem.getAmount();
        if (amount < 1) {
            throw new IllegalArgumentException("Amount must be at least 1.");
        }

        for (int counter = 0; counter < amount; counter++) {
            ReceiptItem singleItem = new ReceiptItem(receiptItem.getPrice(), receiptItem.getName(), 1);
            this.receipt.addReceiptItem(singleItem);
        }
    }


    /**
     * Fills the contactReceiptItems list by creating a ContactReceiptItem for each ReceiptItem
     * in the receipt with the specified contact.
     * @param contact The contact to be associated with all ReceiptItems.
     */
    public void  createContactReceiptItemList(Contact contact) {
        ModelParamValidator.throwIfElementIsNull(contact, "Contact cannot be null.");
        List<ReceiptItem> receiptItems = splitReceiptItems();

        for (ReceiptItem item : receiptItems) {
            ContactReceiptItem contactItem = createContactReceiptItem(item, contact);
            contactReceiptItems.add(contactItem);
        }
    }


    /**
     * Allocates a receipt item to a contact and creates a ContactItem.
     * @param receiptItem The receipt item to allocate.
     * @param contact The contact to which the item is allocated.
     * @return ContactItem representing the allocated receipt item.
     */
    private ContactReceiptItem createContactReceiptItem(ReceiptItem receiptItem, Contact contact) {
        ModelParamValidator.throwIfElementIsNull(receiptItem, "ReceiptItem cannot be null.");
        ModelParamValidator.throwIfElementIsNull(contact, "Contact cannot be null.");
        return new ContactReceiptItem(receiptItem.getPrice(), receiptItem.getName(), contact);
    }



    /**
     * Returns all ContactItems for a given contact.
     * @param contact The contact whose items are to be retrieved.
     * @return List of ContactReceiptItem
     */
    public List<ContactReceiptItem> getContactItemsByContact(Contact contact) {
        ModelParamValidator.throwIfElementIsNull(contact, "Contact cannot be null.");
        return contactReceiptItems.stream()
                .filter(contactReceiptItem -> contactReceiptItem.getContact().equals(contact))
                .collect(Collectors.toList());
    }

    /**
     * Calculates the total debt for a person based on their allocated ContactItems.
     * @param contactItems The list of ContactItems for the contact.
     * @return double representing the total sum of the items.
     */
    public double calculateDebtByPerson(List<ContactReceiptItem> contactItems) {
        ModelParamValidator.throwIfElementIsNull(contactItems, "ContactItems list cannot be null.");
        if (contactItems.isEmpty()) {
            throw new IllegalArgumentException("ContactItems list cannot be empty.");
        }

        double total = 0;
        for (ContactReceiptItem item : contactItems) {
            if (item.getPrice() < 0) {
                throw new IllegalArgumentException("Price cannot be negative.");
            }

            if (!doesContactExist(item.getContact())) {
                throw new IllegalArgumentException("Contact does not exist.");
            }

            total += item.getPrice();
        }
        return total;
    }

    boolean doesContactExist(Contact contact) {
        if (contact == null) {
            return false;
        }
        return contactReceiptItems.stream()
                .anyMatch(item -> item.getContact().equals(contact));
    }
}


