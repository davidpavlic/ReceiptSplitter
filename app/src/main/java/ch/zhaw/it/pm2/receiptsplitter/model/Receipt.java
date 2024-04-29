package ch.zhaw.it.pm2.receiptsplitter.model;

import java.util.Comparator;
import java.util.List;

//TODO: JavaDoc
public class Receipt {

    private List<ReceiptItem> receiptItemList;

    public Receipt(List<ReceiptItem> receiptItem) {
        setReceiptItemList(receiptItem);
    }

    public List<ReceiptItem> getReceiptItemList() {
        return receiptItemList;
    }

    public void setReceiptItemList(List<ReceiptItem> receiptItemList) throws IllegalArgumentException{
        ModelParamValidator.throwIfElementIsNull(receiptItemList, ReceiptErrorMessageType.LIST_NULL.toString());
        this.receiptItemList = receiptItemList;
    }

    public ReceiptItem getReceiptItem(int index) throws IllegalArgumentException{
        ModelParamValidator.throwIfIndexOutOfBounds(index, receiptItemList.size(), ReceiptErrorMessageType.INDEX_NOT_PRESENT.toString());
        return receiptItemList.get(index);
    }

    public float getReceiptTotal(){
        return (float) receiptItemList.stream().mapToDouble(ReceiptItem::getPrice).sum();
    }

    public void addReceiptItem(ReceiptItem receiptItem) throws IllegalArgumentException{
        ModelParamValidator.throwIfElementIsNull(receiptItem, ReceiptErrorMessageType.ITEM_NULL.toString());
        receiptItemList.add(receiptItem);
    }

    public void updateReceiptItem(int index, ReceiptItem receiptItem) throws IllegalArgumentException{
        ModelParamValidator.throwIfIndexOutOfBounds(index, receiptItemList.size(), ReceiptErrorMessageType.INDEX_NOT_PRESENT.toString());
        ModelParamValidator.throwIfElementIsNull(receiptItem, ReceiptErrorMessageType.ITEM_NULL.toString());
        receiptItemList.set(index, receiptItem);
    }

    public void deleteReceiptItem(int index) throws IllegalArgumentException{
        ModelParamValidator.throwIfIndexOutOfBounds(index, receiptItemList.size(), ReceiptErrorMessageType.INDEX_NOT_PRESENT.toString());
        receiptItemList.remove(index);
    }

    public void sortByPriceLowestFirst(){
        sortReceiptItemListBy(Comparator.comparingDouble(ReceiptItem::getPrice));
    }

    public void sortByPriceHighestFirst(){
        sortReceiptItemListBy(Comparator.comparingDouble(ReceiptItem::getPrice).reversed());
    }

    public void sortByNameLowestFirst(){
        sortReceiptItemListBy(Comparator.comparing(ReceiptItem::getName));
    }

    public void sortByNameHighestFirst(){
        sortReceiptItemListBy(Comparator.comparing(ReceiptItem::getName).reversed());
    }

    public void sortByAmountLowestFirst(){
        sortReceiptItemListBy(Comparator.comparingDouble(ReceiptItem::getAmount));
    }

    public void sortByAmountHighestFirst(){
        sortReceiptItemListBy(Comparator.comparingDouble(ReceiptItem::getAmount).reversed());
    }

    private void sortReceiptItemListBy(Comparator<ReceiptItem> comparator) {
        receiptItemList.sort(comparator);
    }

    public enum ReceiptErrorMessageType {
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
