package ch.zhaw.it.pm2.receiptsplitter.model;

import ch.zhaw.it.pm2.receiptsplitter.enums.Currencies;

import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a receipt with a list of receipt items and a Currency.
 * Exposes methods to add, update, delete and get receipt items.
 * The List of Receipt Items  cannot be modified from outside the class.
 * <p>
 * Thr Currency is set to CHF by default.
 */
public class Receipt {

    private Currencies currencies;
    private List<ReceiptItem> receiptItems;

    /**
     * Constructor for a receipt.
     * Creates a new receipt with a list of receipt items.
     *
     * @param receiptItem List of receipt items.
     * @throws IllegalArgumentException If the list is null.
     */
    public Receipt(List<ReceiptItem> receiptItem) {
        currencies = Currencies.CHF; // Default currency
        setReceiptItems(receiptItem);
    }

    /**
     * Set the receipt items.
     *
     * @param receiptItems List of receipt items.
     * @throws IllegalArgumentException If the list is null.
     */
    public void setReceiptItems(List<ReceiptItem> receiptItems) {
        ModelParamValidator.throwIfElementIsNull(receiptItems, ReceiptErrorMessageType.LIST_NULL.toString());
        this.receiptItems = receiptItems;
    }

    // TODO: Remove? Already have a getter for the list
    public ReceiptItem getReceiptItem(int index) {
        ModelParamValidator.throwIfIndexOutOfBounds(index, receiptItems.size(), ReceiptErrorMessageType.INDEX_NOT_PRESENT.toString());
        return receiptItems.get(index);
    }

    /**
     * Get a receipt item by name.
     *
     * @param name Name of the receipt item.
     * @return Optional of the receipt item.
     * @throws IllegalArgumentException If the name is null.
     */
    public Optional<ReceiptItem> getReceiptItemByName(String name) {
        ModelParamValidator.throwIfElementIsNull(name, ReceiptErrorMessageType.ITEM_NULL.toString());
        return receiptItems.stream().filter(item -> item.getName().equals(name)).findFirst();
    }

    /**
     * Calculates the total price of all receipt items.
     *
     * @return Total price of all receipt items.
     */
    public float getReceiptTotal() {
        // TODO: Nice to have in List Items, otherwise I don't know where the sum total of all Receipt Items is needed? Needs to be removed then if not used.
        return (float) receiptItems.stream().mapToDouble(ReceiptItem::getPrice).sum();
    }

    /**
     * Add a receipt item to the list.
     *
     * @param receiptItem Receipt item to be added.
     * @throws IllegalArgumentException If the receipt item is null.
     */
    public void addReceiptItem(ReceiptItem receiptItem) {
        ModelParamValidator.throwIfElementIsNull(receiptItem, ReceiptErrorMessageType.ITEM_NULL.toString());
        receiptItems.add(receiptItem);
    }

    /**
     * Update a receipt item in the list.
     *
     * @param index          Index of the receipt item to be updated.
     * @param newReceiptItem New receipt item.
     * @throws IllegalArgumentException If the index is out of bounds or the new receipt item is null.
     */
    public void updateReceiptItem(int index, ReceiptItem newReceiptItem) {
        ModelParamValidator.throwIfIndexOutOfBounds(index, receiptItems.size(), ReceiptErrorMessageType.INDEX_NOT_PRESENT.toString());
        ModelParamValidator.throwIfElementIsNull(newReceiptItem, ReceiptErrorMessageType.ITEM_NULL.toString());

        ReceiptItem currentReceiptItem = receiptItems.get(index);
        currentReceiptItem.setAmount(newReceiptItem.getAmount());
        currentReceiptItem.setPrice(newReceiptItem.getPrice());
        currentReceiptItem.setName(newReceiptItem.getName());
    }

    /**
     * Delete a receipt item from the list.
     *
     * @param index Index of the receipt item to be deleted.
     * @throws IllegalArgumentException If the index is out of bounds.
     */
    public void deleteReceiptItem(int index) {
        ModelParamValidator.throwIfIndexOutOfBounds(index, receiptItems.size(), ReceiptErrorMessageType.INDEX_NOT_PRESENT.toString());
        receiptItems.remove(index);
    }

    /**
     * Formats a given price as a String with the currency symbol.
     *
     * @param price Price to be formatted.
     * @return Formatted price as a String.
     */
    public String formatPriceWithCurrency(float price) {
        Currency currencyCode = Currency.getInstance(currencies.getCurrency());

        Locale locale = Locale.of("de", "CH"); // de-CH is used as default locale for this MVP Project.

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        numberFormat.setCurrency(currencyCode);

        String formattedPrice = numberFormat.format(price);

        // NumberFormat adds spaces with different unicode points, this is to ensure that the unicode points are the same
        return formattedPrice.replaceAll("[\\s\\u00A0]+", " ").trim();
    }

    /**
     * Get an immutable list of receipt items.
     *
     * @return Immutable list of receipt items.
     */
    public List<ReceiptItem> getReceiptItems() {
        return Collections.unmodifiableList(receiptItems);
    }

    /**
     * Set the currency of the receipt.
     */
    public void setCurrency(Currencies currencies) {
        this.currencies = currencies;
    }

    /**
     * Full copy of receipt items with the elements copied.
     *
     * @param receiptItems List of receipt items to be copied.
     * @return List of copied receipt items.
     */
    public static List<ReceiptItem> fullCopyReceiptItems(List<ReceiptItem> receiptItems) {
        return receiptItems.stream().
                map(item -> new ReceiptItem(item.getPrice(), item.getName(), item.getAmount()))
                .collect(Collectors.toList());
    }


    protected enum ReceiptErrorMessageType {
        LIST_NULL("ReceiptItemList must not be null."),
        ITEM_NULL("ReceiptItem must not be null."),
        NAME_NULL("Name of ReceiptItem must not be null."),
        INDEX_NOT_PRESENT("Index can not be present in list.");

        private final String message;

        ReceiptErrorMessageType(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }
}