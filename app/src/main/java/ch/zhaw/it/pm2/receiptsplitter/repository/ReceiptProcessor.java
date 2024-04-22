package ch.zhaw.it.pm2.receiptsplitter.repository;

import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.model.ContactItem;
import ch.zhaw.it.pm2.receiptsplitter.model.Receipt;

import java.util.List;

public class ReceiptProcessor {
    private static ReceiptProcessor instance;
    private Receipt receipt;
    private List<ContactItem> contactItems;

    private ReceiptProcessor() {
    }

    public static ReceiptProcessor getInstance() {
        if (instance == null) {
            instance = new ReceiptProcessor();
        }
        return instance;
    }

    public void parseReceipt(String extractedImage) {

    }

    public List<ContactItem> getContactItemsByContact(Contact contact) {
           return null;
    }

    public float calculateDebtByContact(List<ContactItem> contactItems) {
        return 0;
    }
}