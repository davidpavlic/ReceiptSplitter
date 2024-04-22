package ch.zhaw.it.pm2.receiptsplitter.model;

public class ContactItem {
    private float price;
    private String name;
    private Contact contact;

    public ContactItem(float price, String name, Contact contact) {
        this.price = price;
        this.name = name;
        this.contact = contact;
    }
}
