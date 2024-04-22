package ch.zhaw.it.pm2.receiptsplitter.repository;

import ch.zhaw.it.pm2.receiptsplitter.model.Contact;

import java.util.List;
import java.util.ArrayList;

public class ContactRepository {
    private List<Contact> contactList;
    private List<Contact> selectedContacts;
    private Contact selectedProfile;

    public ContactRepository() {
        contactList = new ArrayList<>();
        selectedContacts = new ArrayList<>();
    }

    public void loadContact() {
    }

    public boolean addContact(Contact contact) {
        return false;
    }

    public boolean removeContact(String email) {
        return false;
    };

    public List<Contact> getContactList() {
        return contactList;
    }

    public Contact getContact(String email) {
        return null;
    }
}
