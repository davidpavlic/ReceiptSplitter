package ch.zhaw.it.pm2.receiptsplitter.model;

//TODO: JavaDoc
public class ReceiptItem implements CanValidateModelParam {

    private String name;
    private float price;
    private int amount;

    private static final String NAME_ERROR_MESSAGE     = "Name must not be empty.";
    private static final String PRICE_ERROR_MESSAGE    = "Price must not be zero or lower.";
    private static final String AMOUNT_ERROR_MESSAGE   = "Amount must not be zero or lower.";

    public ReceiptItem(float price, String name, int amount) throws IllegalArgumentException{
        setName(name);
        setPrice(price);
        setAmount(amount);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws IllegalArgumentException{
        throwIfStringIsEmpty(name, NAME_ERROR_MESSAGE);
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) throws IllegalArgumentException{
        throwIfZeroOrLower(price, PRICE_ERROR_MESSAGE);
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) throws IllegalArgumentException{
        throwIfZeroOrLower(amount, AMOUNT_ERROR_MESSAGE);
        this.amount = amount;
    }
}
