package ch.zhaw.it.pm2.receiptsplitter.model;

/**
 * This class represents an item in a receipt.
 * It contains the name, price, and amount of the item.
 *
 * @author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 * @version 1.0
 */
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

    /**
     * Sets the name of the item.
     *
     * @param name the name of the item
     * @throws IllegalArgumentException if the name is empty
     */
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

    /**
     * Sets the amount of the item.
     *
     * @param amount the amount of the item
     * @throws IllegalArgumentException if the amount is zero or lower
     */
    public void setAmount(int amount) throws IllegalArgumentException{
        ModelParamValidator.throwIfZeroOrLower(amount, ReceiptItemErrorMessageType.AMOUNT_ZERO_OR_LOWER.toString());
        this.amount = amount;
    }

    /**
     * Rounds the price to two decimal places.
     * @param price the price to round
     * @return the rounded price
     */
    public static float roundPrice(float price) {
        return Math.round(price * 100.0) / 100.0f;
    }

    /**
     * This enum represents the types of error messages that can be thrown by the ReceiptItem class.
     */
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
