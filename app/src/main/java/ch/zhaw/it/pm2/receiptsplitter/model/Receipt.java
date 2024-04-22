package ch.zhaw.it.pm2.receiptsplitter.model;

import java.util.List;

public class Receipt {
    private List<ReceiptItem> receiptItem;

    public Receipt(List<ReceiptItem> receiptItem) {
        this.receiptItem = receiptItem;
    }
}
