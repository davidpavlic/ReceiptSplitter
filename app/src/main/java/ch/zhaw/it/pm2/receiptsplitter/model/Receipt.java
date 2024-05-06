package ch.zhaw.it.pm2.receiptsplitter.model;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

//TODO: JavaDoc
public class Receipt {

    private List<ReceiptItem> receiptItems;

    public Receipt(List<ReceiptItem> receiptItem) {
        setReceiptItems(receiptItem);
    }

    public List<ReceiptItem> getReceiptItems() {
        return receiptItems;
    }

    public void setReceiptItems(List<ReceiptItem> receiptItems) throws IllegalArgumentException{

        ModelParamValidator.throwIfElementIsNull(receiptItems, ReceiptErrorMessageType.LIST_NULL.toString());
        this.receiptItems = receiptItems;
    }

    public ReceiptItem getReceiptItem(int index) throws IllegalArgumentException{
        ModelParamValidator.throwIfIndexOutOfBounds(index, receiptItems.size(), ReceiptErrorMessageType.INDEX_NOT_PRESENT.toString());
        return receiptItems.get(index);
    }

    public float getReceiptTotal(){
        return (float) receiptItems.stream().mapToDouble(ReceiptItem::getPrice).sum();
    }

    public void addReceiptItem(ReceiptItem receiptItem) throws IllegalArgumentException{
        ModelParamValidator.throwIfElementIsNull(receiptItem, ReceiptErrorMessageType.ITEM_NULL.toString());
        receiptItems.add(receiptItem);
    }

    public void updateReceiptItem(int index, ReceiptItem receiptItem) throws IllegalArgumentException{
        ModelParamValidator.throwIfIndexOutOfBounds(index, receiptItems.size(), ReceiptErrorMessageType.INDEX_NOT_PRESENT.toString());
        ModelParamValidator.throwIfElementIsNull(receiptItem, ReceiptErrorMessageType.ITEM_NULL.toString());
        receiptItems.set(index, receiptItem);
    }

    public void deleteReceiptItem(int index) throws IllegalArgumentException{
        ModelParamValidator.throwIfIndexOutOfBounds(index, receiptItems.size(), ReceiptErrorMessageType.INDEX_NOT_PRESENT.toString());
        receiptItems.remove(index);
    }

    public void sortByPriceLowestFirst(){
        sortReceiptItemsBy(Comparator.comparingDouble(ReceiptItem::getPrice));
    }

    public void sortByPriceHighestFirst(){
        sortReceiptItemsBy(Comparator.comparingDouble(ReceiptItem::getPrice).reversed());
    }

    public void sortByNameLowestFirst(){
        sortReceiptItemsBy(Comparator.comparing(ReceiptItem::getName));
    }

    public void sortByNameHighestFirst(){
        sortReceiptItemsBy(Comparator.comparing(ReceiptItem::getName).reversed());
    }

    public void sortByAmountLowestFirst(){
        sortReceiptItemsBy(Comparator.comparingDouble(ReceiptItem::getAmount));
    }

    public void sortByAmountHighestFirst(){
        sortReceiptItemsBy(Comparator.comparingDouble(ReceiptItem::getAmount).reversed());
    }

    /**
     * Full copy of receipt items with the elements copied.
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