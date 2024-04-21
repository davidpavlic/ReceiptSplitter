package ch.zhaw.it.pm2.receiptsplitter.repository;

public class ReceiptItem {
    private float price;
    private String name;
    private int amount;

    public ReceiptItem(float price, String name, int amount) {
        this.price = price;
        this.name = name;
        this.amount = amount;
    }
}
