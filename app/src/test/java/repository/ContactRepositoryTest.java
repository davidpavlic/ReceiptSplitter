package repository;

import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//TODO: add negative test cases
//TODO: Commenting
public class ContactRepositoryTest {

    private static final String CONTACTS_FILE_PATH = "src/test/resources/contacts.csv";
    private static final String TEST_DATA = "John;Doe;John.Doe@example.com\n" +
                                            "Max;Mustermann;Max.Mustermann@example.com\n";

    private static final Contact FIRST_CONTACT = new Contact("John", "Doe", "John.Doe@example.com");
    private static final Contact SECOND_CONTACT = new Contact("Max", "Mustermann", "Max.Mustermann@example.com");
    private static final Contact NEW_CONTACT = new Contact("Third", "Contact", "Third.Contact@example.com");

    private static final int INITIAL_LIST_SIZE = 2;
    private static final int INITIAL_SELECTED_SIZE = 0;

    private ContactRepository contactRepository;

    @BeforeEach
    void setUp() throws Exception{
        resetTestData();
        initializeRepository();
    }

    private void resetTestData(){
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(CONTACTS_FILE_PATH), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(TEST_DATA);
        } catch (IOException e) {
            System.err.println("Failed to initialize test data: " + e.getMessage());
        }
    }

    private void initializeRepository() throws Exception{
        contactRepository = new ContactRepository(CONTACTS_FILE_PATH);
        contactRepository.loadContacts();
    }

    private void assertContactListAttributes(Contact expected, Contact actual){
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getEmail(), actual.getEmail());
    }

    @Test
    void whenLoadingContactRepository_thenAllAttributesAreCorrect(){
        assertEquals(2, contactRepository.getContactList().size());
        assertContactListAttributes(FIRST_CONTACT, contactRepository.getContactList().get(0));
        assertContactListAttributes(SECOND_CONTACT, contactRepository.getContactList().get(1));
    }

    @Test
    void whenAddingAndRemovingSelectedContacts_thenListIsUpdatedCorrectly(){
        contactRepository.addToSelectedContacts(FIRST_CONTACT.getEmail());
        //Selected contacts size should increase by one
        assertEquals(INITIAL_SELECTED_SIZE + 1, contactRepository.getSelectedContacts().size());

        contactRepository.removeFromSelectedContacts(FIRST_CONTACT.getEmail());
        //Selected contacts size should decrease by one
        assertEquals(INITIAL_SELECTED_SIZE, contactRepository.getSelectedContacts().size());
    }

    @Test
    void whenSettingExistingProfile_thenProfileAndListsAreUpdatedCorrectly(){
        contactRepository.setProfile(FIRST_CONTACT.getEmail());

        assertEquals(INITIAL_LIST_SIZE, contactRepository.getContactList().size());
        assertEquals(INITIAL_SELECTED_SIZE + 1, contactRepository.getSelectedContacts().size());

        assertContactListAttributes(FIRST_CONTACT, contactRepository.getProfile());
        assertContactListAttributes(FIRST_CONTACT, contactRepository.getSelectedContacts().get(0));
    }

    @Test
    void whenSettingNewProfile_thenProfileAndListsAreUpdatedCorrectly() throws Exception{
        contactRepository.setNewProfile(NEW_CONTACT);

        //Contact list size should remain unchanged
        assertEquals(INITIAL_LIST_SIZE + 1, contactRepository.getContactList().size());
        //Selected contacts size should increase by 1
        assertEquals(INITIAL_SELECTED_SIZE + 1, contactRepository.getSelectedContacts().size());

        assertContactListAttributes(NEW_CONTACT, contactRepository.getProfile());
        assertContactListAttributes(NEW_CONTACT, contactRepository.getSelectedContacts().get(0));
        assertContactListAttributes(NEW_CONTACT, contactRepository.getContactList().get(2));
    }

    @Test
    void whenReplacingExistingProfile_thenProfileAndListsAreUpdatedCorrectly(){
        contactRepository.setProfile(FIRST_CONTACT.getEmail());
        contactRepository.setProfile(SECOND_CONTACT.getEmail());

        //Contact list size should remain unchanged
        assertEquals(INITIAL_LIST_SIZE, contactRepository.getContactList().size());
        //Selected contacts size should increase by 1
        assertEquals(INITIAL_SELECTED_SIZE + 1, contactRepository.getSelectedContacts().size());

        assertContactListAttributes(SECOND_CONTACT, contactRepository.getProfile());
        assertContactListAttributes(SECOND_CONTACT, contactRepository.getSelectedContacts().get(0));
    }

    @Test
    void whenAddingNewContact_thenListsAreUpdatedCorrectly() throws Exception{
        contactRepository.addContact(NEW_CONTACT);

        //Contact list size should increase by 1
        assertEquals(INITIAL_LIST_SIZE + 1, contactRepository.getContactList().size());
        //Selected contacts size should remain unchanged
        assertEquals(INITIAL_SELECTED_SIZE, contactRepository.getSelectedContacts().size());

        assertContactListAttributes(NEW_CONTACT, contactRepository.getContactList().get(2));
    }

    @Test
    void whenRemovingContact_thenListsAreUpdatedCorrectly() throws Exception{
        contactRepository.addToSelectedContacts(FIRST_CONTACT.getEmail());
        assertEquals(INITIAL_SELECTED_SIZE + 1, contactRepository.getSelectedContacts().size());
        contactRepository.removeContact(FIRST_CONTACT.getEmail());

        //Contact list size should decrease by 1
        assertEquals(INITIAL_LIST_SIZE - 1, contactRepository.getContactList().size());
        //Selected contacts size should decrease by 1
        assertEquals(INITIAL_SELECTED_SIZE, contactRepository.getSelectedContacts().size());

        assertContactListAttributes(SECOND_CONTACT, contactRepository.getContactList().get(0));
    }

    @Test
    void whenRemovingContactAsProfile_thenThrowsException(){
        contactRepository.setProfile(FIRST_CONTACT.getEmail());

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> contactRepository.removeContact(FIRST_CONTACT.getEmail()));
        assertEquals(exception.getMessage(), "Contact is selected profile");
    }

    @Test
    void whenUpdatingContact_thenListsAreUpdatedCorrectly() throws Exception{
        contactRepository.addToSelectedContacts(FIRST_CONTACT.getEmail());
        contactRepository.setProfile(FIRST_CONTACT.getEmail());

        contactRepository.updateContact(FIRST_CONTACT.getEmail(), NEW_CONTACT);
        //Contact List size should remain unchanged
        assertEquals(INITIAL_LIST_SIZE, contactRepository.getContactList().size());
        //Selected contacts size should remain unchanged
        assertEquals(INITIAL_SELECTED_SIZE + 1, contactRepository.getSelectedContacts().size());

        assertContactListAttributes(NEW_CONTACT, contactRepository.getContactList().get(0));
        assertContactListAttributes(NEW_CONTACT, contactRepository.getSelectedContacts().get(0));
        assertContactListAttributes(NEW_CONTACT, contactRepository.getProfile());
    }
}