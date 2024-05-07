package ch.zhaw.it.pm2.receiptsplitter.model;

import ch.zhaw.it.pm2.receiptsplitter.enums.Currency;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//TODO: JavaDoc
public class Receipt {
    private Currency currency;
    private List<ReceiptItem> receiptItems;

    /**
     * Constructor for a receipt.
     * Creates a new receipt with a list of receipt items.
     *
     * @param receiptItem List of receipt items.
     * @throws IllegalArgumentException If the list is null.
     */
    public Receipt(List<ReceiptItem> receiptItem) {
        setReceiptItems(receiptItem);
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
     * Get the total price of all receipt items.
     *
     * @return Total price of all receipt items.
     */
    public float getReceiptTotal() {
        return (float) receiptItems.stream().mapToDouble(ReceiptItem::getPrice).sum();
    }

    /**
     * Add a receipt item to the list.
     *
     * @param receiptItem Receipt item to be added.
     * @throws IllegalArgumentException If the receipt item is null.
     */
    public void addReceiptItem(ReceiptItem receiptItem) throws IllegalArgumentException {
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
    public void updateReceiptItem(int index, ReceiptItem newReceiptItem) throws IllegalArgumentException {
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

    // TODO: Are these sorting methods used? If not, remove (and remove tests too)
    public void sortByPriceLowestFirst() {
        sortReceiptItemsBy(Comparator.comparingDouble(ReceiptItem::getPrice));
    }

    public void sortByPriceHighestFirst() {
        sortReceiptItemsBy(Comparator.comparingDouble(ReceiptItem::getPrice).reversed());
    }

    public void sortByNameLowestFirst() {
        sortReceiptItemsBy(Comparator.comparing(ReceiptItem::getName));
    }

    public void sortByNameHighestFirst() {
        sortReceiptItemsBy(Comparator.comparing(ReceiptItem::getName).reversed());
    }

    public void sortByAmountLowestFirst() {
        sortReceiptItemsBy(Comparator.comparingDouble(ReceiptItem::getAmount));
    }

    public void sortByAmountHighestFirst() {
        sortReceiptItemsBy(Comparator.comparingDouble(ReceiptItem::getAmount).reversed());
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

    private void sortReceiptItemsBy(Comparator<ReceiptItem> comparator) {
        receiptItems.sort(comparator);
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