package ch.zhaw.it.pm2.receiptsplitter.repository;

import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.model.ContactItem;
import ch.zhaw.it.pm2.receiptsplitter.model.Receipt;

import java.util.List;

public class ReceiptProcessor {
    private Receipt receipt;
    private List<ContactItem> contactItems;

    public ReceiptProcessor() {}

    public void parseReceipt(String extractedImage) {

    }

    public List<ContactItem> getContactItemsByContact(Contact contact) {
           return null;
    }

    public float calculateDebtByContact(List<ContactItem> contactItems) {
        return 0;
    }
}