package ch.zhaw.it.pm2.receiptsplitter.repository;

import ch.zhaw.it.pm2.receiptsplitter.model.Contact;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a repository for contacts. It manages the contacts and selected contacts.
 * It also provides methods to add, update, remove and get contacts.
 * <p>
 * A profile can be set which is automatically added to the selected contacts.
 * The profile represents the User who is currently logged in.
 *
 * @author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 * @version 1.0
 */
//TODO: Decide for consistent errormessages. Enum, static string or hard coded.
public class ContactRepository implements IsObservable {
    private static final Logger logger = Logger.getLogger(ContactRepository.class.getName());

    private final List<IsObserver> observers = new ArrayList<>();
    private final List<Contact> contacts = new ArrayList<>();
    private final List<Contact> selectedContacts = new ArrayList<>();

    private Contact selectedProfile;
    private Contact selectedToEditContact;

    private static final String DELIMITER = ";";
    private final Path contactsFilePath;

    /**
     * Sets the path of the contacts file.
     *
     * @param path Path of the contacts file
     */
    public ContactRepository(String path) {
        contactsFilePath = Paths.get(path);
    }

    /**
     * Loads the contacts from the file into the contact list.
     *
     * @throws IOException If an I/O error occurs
     */
    public void loadContacts() throws IOException {
        try (Stream<String> lines = Files.lines(contactsFilePath)) {
            lines.map(this::parseLineToContact).filter(Objects::nonNull).forEach(contacts::add);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addObserver(IsObserver observer) {
        observers.add(observer);
    }

    /**
     * {@inheritDoc}
     */
    public void notifyObservers() {
        logger.fine("Notifying observers in " + this.getClass().getSimpleName());
        for (IsObserver observer : observers) {
            observer.update();
        }
    }

    /**
     * Adds a contact to the contact list.
     *
     * @param contact Contact to be added
     * @throws IOException If an I/O error occurs
     */
    public void addContact(Contact contact) throws IOException {
        if (contactExists(contact.getEmail())) {
            logger.fine("Duplicate contact found with same Email: " + contact.getEmail());
            throw new IllegalArgumentException("Duplicate contact will not be inserted");
        }

        appendContactToContactFile(contact);
        contacts.add(contact);
        notifyObservers();
    }

    /**
     * Updates a contact in the contact list.
     *
     * @param email      Email of the contact to be updated
     * @param newContact New contact to be updated
     * @throws IOException If an I/O error occurs
     */
    public void updateContact(String email, Contact newContact) throws IOException {
        Objects.requireNonNull(email);
        Objects.requireNonNull(newContact);

        if (!contactExists(email)) {
            logger.fine("No record found with email: " + email);
            throw new IllegalArgumentException("No record found with email: " + email);
        }

        if (updateContactInContactFile(email, newContact) && updateContactInContactList(email, newContact)) {
            notifyObservers();
        }
    }

    /**
     * Removes a contact from the contact list.
     *
     * @param email Email of the contact to be removed
     * @return True if the contact is removed, false otherwise
     * @throws IllegalArgumentException If the contact is the selected profile or the contact does not exist
     * @throws IOException              If an I/O error occurs
     */
    public boolean removeContact(String email) throws IllegalArgumentException, IOException {
        if (selectedProfile != null && selectedProfile.getEmail().equals(email)) {
            logger.fine("Contact is selected profile and cannot be removed" + email);
            throw new IllegalArgumentException("Contact is selected profile");
        }

        if (!contactExists(email)) {
            logger.fine("No record found with email: " + email);
            throw new IllegalArgumentException("No record found with email: " + email);
        }

        if (removeContactFromContactFile(email)) {
            if (contacts.removeIf(contact -> contact.getEmail().equals(email))) {
                removeFromSelectedContacts(email);
                notifyObservers();
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a contact to the selected contacts list.
     *
     * @param email Email of the contact to be added
     */
    public void addToSelectedContacts(String email) {
        if (contactExists(email)) {
            selectedContacts.add(findContactByEmail(email).isPresent() ? findContactByEmail(email).get() : null);
        }
    }

    /**
     * Removes a contact from the selected contacts list.
     *
     * @param email Email of the contact to be removed
     */
    public void removeFromSelectedContacts(String email) {
        selectedContacts.removeIf(contact -> contact.getEmail().equals(email));
    }

    /**
     * Removes all contacts from the selected contacts list.
     */
    public void removeAllSelectedContacts() {
        selectedContacts.clear();
    }

    /**
     * Sets the profile to the selected contact.
     *
     * @param email Email of the contact
     * @return True if the profile is set, false otherwise
     */
    public boolean setProfile(String email) {
        String removeEmail = (selectedProfile != null) ? selectedProfile.getEmail() : email;
        removeFromSelectedContacts(removeEmail);

        Optional<Contact> profileOptional = findContactByEmail(email);
        if (profileOptional.isEmpty()) return false;

        selectedProfile = profileOptional.get();
        return selectedContacts.add(selectedProfile);
    }

    /**
     * Sets a new profile and adds it as a new Contact first.
     *
     * @param contact Contact to be set as profile
     * @throws IOException              If an I/O error occurs
     * @throws IllegalArgumentException If the contact already exists
     */

    public void setNewProfile(Contact contact) throws IOException {
        logger.info("Setting new profile: " + contact.getEmail());
        addContact(contact);
        setProfile(contact.getEmail());
    }

    /**
     * Gets the selected contacts.
     *
     * @return List of selected contacts
     */
    public List<Contact> getSelectedContacts() {
        return selectedContacts;
    }

    /**
     * Gets the selected contact to edit.
     *
     * @return Contact to edit
     */
    public Contact getSelectedToEditContact() {
        return selectedToEditContact;
    }

    /**
     * Gets the contact list.
     *
     * @return List of contacts
     */
    public Contact getProfile() {
        return selectedProfile;
    }

    /**
     * Gets the contact list.
     *
     * @return List of contacts
     */
    public List<Contact> getContacts() {
        return contacts;
    }

    /**
     * Sets the selected contact to edit.
     *
     * @param selectedToEditContact Contact to edit
     */
    public void setSelectedToEditContact(Contact selectedToEditContact) {
        this.selectedToEditContact = selectedToEditContact;
    }

    private boolean updateContactInContactList(String email, Contact newContact) {
        for (Contact contact : contacts) {
            if (contact.getEmail().equals(email)) {
                contact.setFirstName(newContact.getFirstName());
                contact.setLastName(newContact.getLastName());
                contact.setEmail(newContact.getEmail());
                return true;
            }
        }
        return false;
    }

    private boolean updateContactInContactFile(String email, Contact newContact) throws IOException {
        List<String> updatedLines = getUpdatedContactsFromEmail(email, Files.readAllLines(contactsFilePath), newContact);
        if (updatedLines != null) {
            Files.write(contactsFilePath, updatedLines);
            return true;
        }
        return false;
    }

    private List<String> getUpdatedContactsFromEmail(String email, List<String> lines, Contact newContact) {
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

    private boolean removeContactFromContactFile(String email) throws IOException {
        List<String> contactListAllLines = Files.readAllLines(contactsFilePath);
        List<String> contactListNotEmail = contactListAllLines.stream()
                .filter(line -> !line.contains(email))
                .collect(Collectors.toList());

        if (!contactListAllLines.equals(contactListNotEmail)) {
            // Remove the last line if it is empty
            if (!contactListNotEmail.isEmpty() && contactListNotEmail.getLast().isEmpty()) {
                contactListNotEmail.removeLast();
            }
            Files.write(contactsFilePath, contactListNotEmail);
            return true;
        }
        return false;
    }

    private void appendContactToContactFile(Contact contact) throws IOException {
        String lastLine = Files.readAllLines(contactsFilePath).getLast();

        try (BufferedWriter writer = Files.newBufferedWriter(contactsFilePath, StandardOpenOption.APPEND)) {
            if (!lastLine.isEmpty()) {
                writer.newLine();
            }

            writer.write(parseContactToLine(contact));
        }
    }

    private Optional<Contact> findContactByEmail(String email) {
        return contacts.stream()
                .filter(contact -> contact.getEmail().equals(email))
                .findFirst();
    }

    private boolean contactExists(String email) {
        return findContactByEmail(email).isPresent();
    }

    private Contact parseLineToContact(String line) {
        String[] values = line.split(DELIMITER);
        if (values.length >= 3) {
            return new Contact(values[0], values[1], values[2]);
        }
        return null;
    }

    private String parseContactToLine(Contact contact) {
        return String.format("%s%s%s%s%s",
                contact.getFirstName(), DELIMITER,
                contact.getLastName(), DELIMITER,
                contact.getEmail());
    }
}
