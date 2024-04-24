package ch.zhaw.it.pm2.receiptsplitter.model;

public class ContactReceiptItem {
    private float price;
    private String name;
    private Contact contact;

    public ContactReceiptItem(float price, String name, Contact contact) {
        this.price = price;
        this.name = name;
        this.contact = contact;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
