package ch.zhaw.it.pm2.receiptsplitter.model;

//TODO: JavaDoc
public class ContactReceiptItem implements CanValidateModelParam {

    private float price;
    private String name;
    private Contact contact;

    private static final String PRICE_ERROR_MESSAGE    = "Price must not be zero or lower.";
    private static final String NAME_ERROR_MESSAGE     = "Name must not be empty.";
    private static final String CONTACT_ERROR_MESSAGE  = "Contact must not be null.";

    public ContactReceiptItem(float price, String name, Contact contact) {
        setPrice(price);
        setName(name);
        setContact(contact);
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) throws IllegalArgumentException{
        throwIfZeroOrLower(price, PRICE_ERROR_MESSAGE);
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws IllegalArgumentException{
        throwIfStringIsEmpty(name, NAME_ERROR_MESSAGE);
        this.name = name;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) throws IllegalArgumentException{
        throwIfElementIsNull(contact, CONTACT_ERROR_MESSAGE);
        this.contact = contact;
    }
}
