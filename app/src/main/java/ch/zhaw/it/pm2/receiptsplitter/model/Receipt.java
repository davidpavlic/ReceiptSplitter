package ch.zhaw.it.pm2.receiptsplitter.model;

import java.util.List;

public class Receipt {
    private List<ReceiptItem> receiptItemList;

    public Receipt(List<ReceiptItem> receiptItem) {
        this.receiptItemList = receiptItem;
    }

    public List<ReceiptItem> getReceiptItemList() {
        return receiptItemList;
    }

    public void setReceiptItemList(List<ReceiptItem> receiptItemList) {
        this.receiptItemList = receiptItemList;
    }

    public void addReceiptItem(ReceiptItem receiptItem){
        receiptItemList.add(receiptItem);
    }

    public void deleteReceiptItem(int index){
        receiptItemList.remove(index);
    }

    public void updateReceiptItem(int index, ReceiptItem receiptItem){
        receiptItemList.set(index, receiptItem);
    }
}
