package ch.zhaw.it.pm2.receiptsplitter.model;

public class ReceiptItem {
    private String name;
    private float price;
    private int amount;

    public ReceiptItem(float price, String name, int amount) throws IllegalArgumentException{
        setName(name);
        setPrice(price);
        setAmount(amount);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws IllegalArgumentException{
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name must not be empty.");
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) throws IllegalArgumentException{
        if (price <= 0)
            throw new IllegalArgumentException("Price must not be zero or lower.");
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) throws IllegalArgumentException{
        if (amount <= 0)
            throw new IllegalArgumentException("Amount must not be zero or lower.");
        this.amount = amount;
    }
}
