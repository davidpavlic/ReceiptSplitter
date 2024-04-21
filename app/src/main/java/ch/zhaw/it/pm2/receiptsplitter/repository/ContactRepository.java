package ch.zhaw.it.pm2.receiptsplitter.repository;

import java.util.List;
import java.util.ArrayList;

public class ContactRepository {
    private static ContactRepository contactRepository;
    private List<Contact> contactList;
    private List<Contact> selectedContacts;
    private Contact selectedProfile;

    private ContactRepository() {
        contactList = new ArrayList<>();
        selectedContacts = new ArrayList<>();
    }

    public static ContactRepository getInstance() {
        if (contactRepository == null) {
            contactRepository = new ContactRepository();
        }
        return contactRepository;
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
