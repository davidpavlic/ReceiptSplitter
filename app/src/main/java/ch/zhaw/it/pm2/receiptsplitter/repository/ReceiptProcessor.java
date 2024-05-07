package ch.zhaw.it.pm2.receiptsplitter.repository;

import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.model.ContactReceiptItem;
import ch.zhaw.it.pm2.receiptsplitter.model.Receipt;
import ch.zhaw.it.pm2.receiptsplitter.model.ReceiptItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Manages receipt-related operations including item management and allocation to contacts.
 * The ReceiptItems added to the Receipt should be unique by name.
 *
 */
public class ReceiptProcessor implements IsObservable {
    private Receipt receipt;
    private List<ContactReceiptItem> contactReceiptItems;

    private final List<IsObserver> observers = new ArrayList<>();

    /**
     * Constructs a new ReceiptProcessor instance with an empty list of contact-receipt-item associations.
     */
    public ReceiptProcessor() {
        this.contactReceiptItems = new ArrayList<>();
        this.receipt = new Receipt(new ArrayList<>());
    }

    /**
     * @inheritDoc
     */
    @Override
    public void addObserver(IsObserver observer) {
        observers.add(observer);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void notifyObservers() {
        // TODO: Separate the notify for receipt and contactReceiptItems changes
        for (IsObserver observer : observers) {
            observer.update();
        }
    }

    /**
     * Adds a new ReceiptItem to the list of receipt items.
     * Validates the ReceiptItem to have a unique name and not be null.
     * Notifies observers of the change.
     *
     * @param receiptItem the new ReceiptItem to be added
     * @throws IllegalArgumentException if the receiptItem is null or already exists
     */
    public void addReceiptItem(ReceiptItem receiptItem) {
        if (receiptItem == null) throw new IllegalArgumentException("Receipt item cannot be null.");

        List<ReceiptItem> receiptItems = receipt.getReceiptItems();
        boolean receiptItemExists = receiptItems.stream().anyMatch(item -> item.getName().equals(receiptItem.getName()));
        if (receiptItemExists) {
            throw new IllegalArgumentException("Receipt item already exists.");
        }

        receiptItems.add(receiptItem);
        notifyObservers();
    }


    /**
     * Updates an existing ReceiptItem in the list of receipt items.
     * Notifies observers of the change.
     *
     * @param newReceiptItem the ReceiptItem to be updated
     * @throws IllegalArgumentException if the newReceiptItem is null, the old name is null, the item with the old name does not exist or the new name already exists.
     */
    public void updateReceiptItemByName(String oldName, ReceiptItem newReceiptItem) {
        if (oldName == null) throw new IllegalArgumentException("Old name cannot be null.");
        if (newReceiptItem == null) throw new IllegalArgumentException("Receipt item cannot be null.");

        List<ReceiptItem> receiptItems = receipt.getReceiptItems();
        Optional<ReceiptItem> receiptItemOptional = receiptItems.stream().filter(item -> item.getName().equals(oldName)).findFirst();

        if (receiptItemOptional.isEmpty()) {
            throw new IllegalArgumentException("Receipt item does not exist.");
        }

        if (!oldName.equals(newReceiptItem.getName())) {
            boolean newNameExists = receiptItems.stream().anyMatch(item -> item.getName().equals(newReceiptItem.getName()));
            if (newNameExists) {
                throw new IllegalArgumentException("Receipt item with the new specified name already exists.");
            }
        }

        ReceiptItem receiptItem = receiptItemOptional.get();
        receiptItem.setAmount(newReceiptItem.getAmount());
        receiptItem.setName(newReceiptItem.getName());
        receiptItem.setPrice(newReceiptItem.getPrice());

        notifyObservers();
    }


    /**
     * Removes a specified ReceiptItem from the list.
     * Notifies observers of the change.
     *
     * @param name the name of the ReceiptItem to be removed
     * @return true if the item was removed, false otherwise
     * @throws IllegalArgumentException if the name is null or if the item does not exist
     */
    public boolean deleteReceiptItemByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Receipt item cannot be null.");
        }

        Optional<ReceiptItem> receiptItemOptional = receipt.getReceiptItems().stream().filter(item -> item.getName().equals(name)).findFirst();

        if (receiptItemOptional.isEmpty()) {
            throw new IllegalArgumentException("Receipt item does not exist.");
        }

        List<ReceiptItem> receiptItems = receipt.getReceiptItems();
        ReceiptItem itemToRemove = receiptItemOptional.get();
        boolean removeSuccess = receiptItems.remove(itemToRemove);

        if (removeSuccess) notifyObservers();

        return removeSuccess;
    }

    /**
     * Associates a contact with a ReceiptItem.
     * Notifies observers of the change.
     *
     * @param contact     the contact to be associated
     * @param receiptItem the ReceiptItem to associate
     * @throws IllegalArgumentException if the receiptItem is null or doesn't exist
     */
    public void createContactReceiptItem(Contact contact, ReceiptItem receiptItem) {
        if (receiptItem == null) throw new IllegalArgumentException("Receipt item cannot be null.");

        boolean receiptItemExists = receipt.getReceiptItems().stream().anyMatch(item -> item.getName().equals(receiptItem.getName()));
        if (!receiptItemExists) {
            throw new IllegalArgumentException("Receipt item does not exist.");
        }

        contactReceiptItems.add(new ContactReceiptItem(receiptItem.getPrice(), receiptItem.getName(), contact));
        notifyObservers();
    }

    /**
     * Splits bulk items in the receipt into individual items to handle scenarios where items bought together
     * need to be billed separately.
     *
     * @return a list of individually split ReceiptItems
     */
    public List<ReceiptItem> splitReceiptItems() {
        List<ReceiptItem> splitReceiptItems = new ArrayList<>();
        List<ReceiptItem> receiptItems = receipt.getReceiptItems();

        for (ReceiptItem receiptItem : receiptItems) {
            List<ReceiptItem> splitReceiptItem = splitIntoIndividualReceiptItems(receiptItem);
            splitReceiptItems.addAll(splitReceiptItem);
        }
        return splitReceiptItems;
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
     * @throws IllegalArgumentException if no items are associated with the contact.
     */
    public double calculateDebtByPerson(Contact contact) {
        List<ContactReceiptItem> contactItemsByContact = getContactItemsByContact(contact.getEmail());
        if (contactItemsByContact.isEmpty()) {
            throw new IllegalArgumentException("The list is empty.");
        }

        double total = 0;

        for (ContactReceiptItem contactReceiptItem : contactItemsByContact) {
            total += contactReceiptItem.getPrice();
        }

        return total;
    }

    /**
     * Retrieves the list of receipt items as a copy with every item being copied too, to prevent direct modification.
     *
     * @return a copied list of receipt items wih its items copied
     */
    public List<ReceiptItem> getFullCopyReceiptItems() {
        return Receipt.fullCopyReceiptItems(receipt.getReceiptItems());
    }

    /**
     * Sets a new list of receipt items to replace the existing one.
     * Notifies observers of the change.
     *
     * @param receiptItems the new list of receipt items
     */
    public void setReceiptItems(List<ReceiptItem> receiptItems) {
        receipt.setReceiptItems(receiptItems);
        notifyObservers();
    }

    /**
     * Retrieves the distinct Contacts of the contact-receipt items.
     *
     * @return List of distinct Contacts
     */
    public List<Contact> getDistinctContacts() {
        return contactReceiptItems.stream()
                .map(ContactReceiptItem::getContact)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Adds or replaces the current receipt. If a receipt is already present, it will be replaced.
     *
     * @param receipt the new receipt to be managed
     * @throws IllegalArgumentException if the provided receipt is null
     */
    public void setReceipt(Receipt receipt) {
        if (receipt == null) {
            throw new IllegalArgumentException("Receipt cannot be null");
        }
        this.receipt = receipt;
    }

    /**
     * Returns the current list of all associated contact-receipt items.
     *
     * @return a list of ContactReceiptItems
     */
    public List<ContactReceiptItem> getContactReceiptItems() {
        return contactReceiptItems;
    }

    /**
     * Updates the internal list of contact-receipt items with a new list.
     *
     * @param contactReceiptItems the new list of ContactReceiptItem to replace the existing one
     */
    public void setContactReceiptItems(List<ContactReceiptItem> contactReceiptItems) {
        this.contactReceiptItems = contactReceiptItems;
    }

    /**
     * Splits a single ReceiptItem into multiple items based on its quantity, setting each item's quantity to 1.
     *
     * @param receiptItem the ReceiptItem to be split
     * @return a list of individual ReceiptItems
     */
    private List<ReceiptItem> splitIntoIndividualReceiptItems(ReceiptItem receiptItem) {
        List<ReceiptItem> splitReceiptItem = new ArrayList<>();
        int amount = receiptItem.getAmount();

        for (int counter = 0; counter < amount; counter++) {
            splitReceiptItem.add(new ReceiptItem(receiptItem.getPrice() / amount, receiptItem.getName(), 1));
        }

        return splitReceiptItem;
    }
}


