package ch.zhaw.it.pm2.receiptsplitter.model;

/**
 * This class represents an item in a receipt that is associated with a contact.
 * It contains the name, price of the item, and the contact who will pay for the item.
 *
 * @author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 * @version 1.0
 */
public class ContactReceiptItem {

    private float price;
    private String name;
    private Contact contact;

    /**
     * Constructs a new ContactReceiptItem instance.
     *
     * @param price the price of the item
     * @param name the name of the item
     * @param contact the contact who will pay for the item
     * @throws IllegalArgumentException if the name is empty, or if the price is zero or lower, or if the contact is null
     */
    public ContactReceiptItem(float price, String name, Contact contact) {
        setPrice(price);
        setName(name);
        setContact(contact);
    }

    public float getPrice() {
        return price;
    }

    /**
     * Sets the price of the item.
     *
     * @param price the price of the item
     * @throws IllegalArgumentException if the price is zero or lower
     */
    public void setPrice(float price) throws IllegalArgumentException{
        ModelParamValidator.throwIfZeroOrLower(price, ContactReceiptItemErrorMessageType.PRICE_ZERO_OR_LOWER.toString());
        this.price = price;
    }

    public String getName() {
        return name;
    }

    /**
     * Sets the name of the item.
     *
     * @param name the name of the item
     * @throws IllegalArgumentException if the name is empty
     */
    public void setName(String name) throws IllegalArgumentException{
        ModelParamValidator.throwIfStringIsEmpty(name, ContactReceiptItemErrorMessageType.NAME_EMPTY.toString());
        this.name = name;
    }

    public Contact getContact() {
        return contact;
    }

    /**
     * Sets the contact associated with the item.
     *
     * @param contact the contact to associate with the item
     * @throws IllegalArgumentException if the contact is null
     */
    public void setContact(Contact contact) throws IllegalArgumentException{
        ModelParamValidator.throwIfElementIsNull(contact, ContactReceiptItemErrorMessageType.CONTACT_NULL.toString());
        this.contact = contact;
    }

    /**
     * This enum represents the types of error messages that can be thrown by the ContactReceiptItem class.
     */
    protected enum ContactReceiptItemErrorMessageType {
        PRICE_ZERO_OR_LOWER("Price must not be zero or lower."),
        NAME_EMPTY("Name must not be empty."),
        CONTACT_NULL("Contact must not be null.");

        private final String message;

        ContactReceiptItemErrorMessageType(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }
}
