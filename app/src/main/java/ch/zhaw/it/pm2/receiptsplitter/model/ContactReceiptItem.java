package ch.zhaw.it.pm2.receiptsplitter.model;

//TODO: JavaDoc
public class ContactReceiptItem implements CanValidateModelParam {

    private float price;
    private String name;
    private Contact contact;

    public ContactReceiptItem(float price, String name, Contact contact) {
        setPrice(price);
        setName(name);
        setContact(contact);
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) throws IllegalArgumentException{
        throwIfZeroOrLower(price, ContactReceiptItemErrorMessageType.PRICE_ZERO_OR_LOWER.toString());
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws IllegalArgumentException{
        throwIfStringIsEmpty(name, ContactReceiptItemErrorMessageType.NAME_EMPTY.toString());
        this.name = name;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) throws IllegalArgumentException{
        throwIfElementIsNull(contact, ContactReceiptItemErrorMessageType.CONTACT_NULL.toString());
        this.contact = contact;
    }

    public enum ContactReceiptItemErrorMessageType {
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
