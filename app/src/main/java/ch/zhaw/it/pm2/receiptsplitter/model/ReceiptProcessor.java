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
    private List<ContactReceiptItem> contactReceiptItemList;

    public ReceiptProcessor(Receipt receipt) {
        ModelParamValidator.throwIfElementIsNull(receipt, "Receipt cannot be null.");
        this.receipt = receipt;
        this.contactReceiptItemList = new ArrayList<>();
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


    /**
     * Splits each {@link ReceiptItem} in the current receipt into individual items.
     * This method retrieves a list of {@link ReceiptItem} objects from the current receipt,
     * processes each item to split it into individual components if necessary, and then
     * returns the modified list of {@link ReceiptItem} objects.
     * <p>
     * This is particularly useful when a {@link ReceiptItem} contains multiple units that need
     * to be handled separately (e.g., when items are purchased in bulk but need to be sold individually).
     * Each item in the list is processed by the {@code splitIntoIndividualReceiptItems} method
     * which should handle the logic for splitting the items according to the business rules.
     * </p>
     *
     * @return a list of {@link ReceiptItem} where each item has been potentially split into more
     *         detailed components, reflecting the individual units rather than aggregated totals.
     */
    public List<ReceiptItem> splitReceiptItems(){
        List<ReceiptItem> receiptItems = getReceiptItems();

        for (ReceiptItem receiptItem : receiptItems) {
            splitIntoIndividualReceiptItems(receiptItem);
        }
        return receiptItems;
    }

    /**
     * Adds a ReceiptItem to the list. If the amount is more than one, it adds
     * multiple instances of the ReceiptItem to the list according to the amount.
     *
     * @param receiptItem The ReceiptItem to be added.
     */
    private void splitIntoIndividualReceiptItems(ReceiptItem receiptItem) throws IllegalArgumentException{
        int amount = receiptItem.getAmount();
        for (int counter = 0; counter < amount; counter++) {
            this.receipt.addReceiptItem(new ReceiptItem(receiptItem.getPrice(), receiptItem.getName(), 1));
        }
    }

    /**
     * Replaces the {@link ReceiptItem} at the specified position in the list of receipt items with the provided {@link ReceiptItem}.
     * If the specified position exceeds the current list size or other exceptions occur, an exception is thrown.
     *
     * @param receiptItem The {@link ReceiptItem} to be set at the specified index in the list. This object contains the data relevant to the receipt item that needs to be created or updated.
     * @param rowId The index at which the receipt item should be updated or inserted. This index is zero-based and must be a valid index within the current list size.
     * @throws Exception Throws an exception if the `rowId` is out of the valid index range of the list or any other issues occur during the operation. This includes handling for index out of bounds exceptions.
     */
    public void createOrUpdateReceiptItem(ReceiptItem receiptItem, int rowId) throws Exception {
        List<ReceiptItem> receiptItems = getReceiptItems();
        receiptItems.set(rowId, receiptItem);
        }


    /**
     * Removes the {@link ReceiptItem} at the specified position in the list of receipt items.
     * This method shifts any subsequent elements to the left (subtracts one from their indices).
     * If the specified position exceeds the current list size or other exceptions occur, an exception is thrown.
     *
     * @param rowId The index of the {@link ReceiptItem} to be removed. This index is zero-based and must be a valid index within the current list size.
     * @throws Exception Throws an exception if the `rowId` is out of the valid index range of the list or any other issues occur during the removal process. This includes handling for index out of bounds exceptions.
     */
    public void deleteReceiptItem(int rowId) throws Exception {
        List<ReceiptItem> receiptItems = getReceiptItems();
        receiptItems.remove(rowId);
    }


    /**
     * Fills the contactReceiptItems list by creating a ContactReceiptItem for each ReceiptItem
     * in the receipt with the specified contact.
     * @param contact The contact to be associated with all ReceiptItems.
     */
    public void  createContactReceiptItem(Contact contact, int rowId) {
        ReceiptItem receiptItem =  getReceiptItems().get(rowId);
        contactReceiptItemList.add(new ContactReceiptItem(receiptItem.getPrice(), receiptItem.getName(),  contact));
    }



    /**
     * Returns all ContactItems for a given contact.
     * @param contact The contact whose items are to be retrieved.
     * @return List of ContactReceiptItem
     */
    public List<ContactReceiptItem> getContactItemsByContact(Contact contact) {
        return contactReceiptItemList.stream()
                .filter(contactReceiptItem -> contactReceiptItem.getContact().equals(contact))
                .collect(Collectors.toList());
    }


    /**
     * Calculates the total debt amount by a specific contact by summing the prices of all receipt items
     * associated with this contact. This method fetches a list of receipt items linked to the contact
     * and calculates their cumulative price.
     *
     * @param contact the contact for whom the debt is to be calculated.
     * @return the total amount owed by the contact.
     * @throws IllegalArgumentException if any receipt item refers to a contact that does not exist.
     */
    public double calculateDebtByPerson(Contact contact) throws IllegalArgumentException {
        // Fetch receipt items associated with the specified contact
        List<ContactReceiptItem> specificContactItemsList = getContactItemsByContact(contact);
        double total = 0;

        for (ContactReceiptItem contactReceiptItem : specificContactItemsList) {
            if (!doesContactExist(contactReceiptItem.getContact())) {
                throw new IllegalArgumentException("Contact does not exist.");
            }
            total += contactReceiptItem.getPrice();
        }
        return total;
    }


    boolean doesContactExist(Contact contact) {
        if (contact == null) {
            return false;
        }
        return contactReceiptItemList.stream()
                .anyMatch(item -> item.getContact().equals(contact));
    }

    /**
     * Method to get a list of all receipt items along with their prices.
     * @return List of ReceiptItem
     */
    public List<ReceiptItem> getReceiptItems() {
        return new ArrayList<>(receipt.getReceiptItemList());
    }

    public List<ContactReceiptItem> getContactReceiptItems(){
        return contactReceiptItemList;
    }

    public  void setContactReceiptItems(List<ContactReceiptItem> contactReceiptItems){
        this.contactReceiptItemList = contactReceiptItems;
    }
}


