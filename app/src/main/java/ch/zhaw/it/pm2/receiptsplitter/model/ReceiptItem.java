package ch.zhaw.it.pm2.receiptsplitter.model;

//TODO: JavaDoc
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
        ModelParamValidator.throwIfStringIsEmpty(name, ReceiptItemErrorMessageType.NAME_EMPTY.toString());
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) throws IllegalArgumentException{
        ModelParamValidator.throwIfZeroOrLower(price, ReceiptItemErrorMessageType.PRICE_ZERO_OR_LOWER.toString());
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) throws IllegalArgumentException{
        ModelParamValidator.throwIfZeroOrLower(amount, ReceiptItemErrorMessageType.AMOUNT_ZERO_OR_LOWER.toString());
        this.amount = amount;
    }

    protected enum ReceiptItemErrorMessageType {
        NAME_EMPTY("Name must not be empty."),
        PRICE_ZERO_OR_LOWER("Price must not be zero or lower."),
        AMOUNT_ZERO_OR_LOWER("Amount must not be zero or lower.");

        private final String message;

        ReceiptItemErrorMessageType(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }
}
