package ch.zhaw.it.pm2.receiptsplitter.repository;

import ch.zhaw.it.pm2.receiptsplitter.model.Contact;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

//TODO: Slight Refactoring
//TODO: JavaDoc
public class ContactRepository {
    private final List<Contact> contactList = new ArrayList<>();
    private final List<Contact> selectedContacts = new ArrayList<>();
    private Contact selectedProfile;

    private static final String DELIMITER = ";";
    private final Path contactsFilePath;

    //Constructor defines the filepath
    public ContactRepository(String path) {
        contactsFilePath = Paths.get(path);
    }

    //Loads the contacts from the file into the contact list
    public void loadContacts() throws IOException{
        Files.lines(contactsFilePath).map(this::parseLineToContact).filter(Objects::nonNull).forEach(contactList::add);
    }

    //The following methods represent CRUD operations for the selected profile
    public Contact getProfile(){
        return selectedProfile;
    }

    public void setProfile(String email){
        removeProfileFromSelectedContacts(email);
        Contact profile = findContactByEmail(email);
        selectedProfile = profile;
        selectedContacts.add(profile);
    }

    public void setNewProfile(Contact contact) throws IOException {
        if(addContact(contact))
            setProfile(contact.getEmail());
    }

    //Returns the contact list
    public List<Contact> getContactList() {
        return contactList;
    }

    //Checks if a contact exists
    public boolean contactExists(String email){
        return findContactByEmail(email) != null;
    }

    //The following methods represent CRUD operations for contacts adjusting the file, contactlist, selectedContact and selectedProfile
    public Contact findContactByEmail(String email) {
        for (Contact contact : contactList) {
            if (contact.getEmail().equals(email)) {
                return contact;
            }
        }
        return null;
    }

    public boolean addContact(Contact contact) throws IllegalArgumentException, IOException {
        if(contactExists(contact.getEmail()))
            throw new IllegalArgumentException("Duplicate contact will not be inserted");

        return appendContactToContactFile(contact);
    }

    public boolean updateContact(String email, Contact newContact) throws IllegalArgumentException, IOException {
        if(!contactExists(email))
            throw new IllegalArgumentException("No record found with email: " + email);
        return updateContactInContactFile(email, newContact) && updateContactInContactList(email, newContact);
    }

    public boolean removeContact(String email) throws IllegalArgumentException, IOException {
        if(selectedProfile != null && selectedProfile.getEmail().equals(email))
            throw new IllegalArgumentException("Contact is selected profile");
        if(!contactExists(email))
            throw new IllegalArgumentException("No record found with email: " + email);

        if(removeContactFromContactFile(email))
            return contactList.removeIf(contact -> contact.getEmail().equals(email)) && removeFromSelectedContacts(email);
        return false;
    }

    //The following methods represent CRUD operations for selected contacts list
    public List<Contact> getSelectedContacts() {
        return selectedContacts;
    }

    public boolean addToSelectedContacts(String email){
        if(contactExists(email)){
            selectedContacts.add(findContactByEmail(email));
            return true;
        }
        return false;
    }

    public boolean removeFromSelectedContacts(String email){
        return selectedContacts.removeIf(contact -> contact.getEmail().equals(email));
    }

    //The following method is a helper method for updating a contact in the contact list
    private boolean updateContactInContactList(String email, Contact newContact){
        for (Contact contact : contactList) {
            if (contact.getEmail().equals(email)) {
                contact.setFirstName(newContact.getFirstName());
                contact.setLastName(newContact.getLastName());
                contact.setEmail(newContact.getEmail());
                return true;
            }
        }
        return false;
    }

    //The following method is a profile helper method
    private void removeProfileFromSelectedContacts(String email){
        if(selectedProfile != null)
            removeFromSelectedContacts(selectedProfile.getEmail());
        else
            removeFromSelectedContacts(email);
    }

    //The following methods represent file CRUD operations
    private boolean updateContactInContactFile(String email, Contact newContact) throws IOException{
        List<String> updatedLines = getUpdatedContactsFromEmail(email, Files.readAllLines(contactsFilePath), newContact);
        if(updatedLines != null){
            Files.write(contactsFilePath, updatedLines);
            return true;
        }
        return false;
    }

    private List<String> getUpdatedContactsFromEmail(String email, List<String> lines, Contact newContact){
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        for (String line : lines) {
            if (line.contains(email)) {
                updatedLines.add(parseContactToLine(newContact));
                found = true;
            } else {
                updatedLines.add(line);
            }
        }

        return found ? updatedLines : null;
    }

    private boolean removeContactFromContactFile(String email) throws IOException{
        List<String> contactListAllLines = Files.readAllLines(contactsFilePath);
        List<String> contactListNotEmail = contactListAllLines.stream().filter(line -> !line.contains(email)).collect(Collectors.toList());
        Files.write(contactsFilePath, contactListNotEmail);
        return true;
    }

    private boolean appendContactToContactFile(Contact contact) throws IOException{
        BufferedWriter writer = Files.newBufferedWriter(contactsFilePath, StandardOpenOption.APPEND);
        writer.write(parseContactToLine(contact));
        contactList.add(contact);
        return true;

    }

    //The following methods parse between line and contact
    private Contact parseLineToContact(String line){
        String[] values = line.split(DELIMITER);
        if (values.length >= 3) {
            return new Contact(values[0], values[1], values[2]);
        }
        return null;
    }

    private String parseContactToLine(Contact contact) {
        return String.format("%s%s%s%s%s%s%n",
                contact.getFirstName(), DELIMITER,
                contact.getLastName(), DELIMITER,
                contact.getEmail(), DELIMITER);
    }
}
