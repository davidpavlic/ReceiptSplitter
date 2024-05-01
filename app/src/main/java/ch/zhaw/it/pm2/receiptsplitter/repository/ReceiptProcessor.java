package ch.zhaw.it.pm2.receiptsplitter.repository;

import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.model.ContactReceiptItem;
import ch.zhaw.it.pm2.receiptsplitter.model.Receipt;

import java.util.List;

public class ReceiptProcessor {
    private Receipt receipt;
    private List<ContactReceiptItem> contactItems;

    public ReceiptProcessor() {}

    public void parseReceipt(String extractedImage) {

    }

    public List<ContactReceiptItem> getContactItemsByContact(Contact contact) {
           return null;
    }

    public float calculateDebtByContact(List<ContactReceiptItem> contactItems) {
        return 0;
    }
}