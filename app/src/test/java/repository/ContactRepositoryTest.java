package repository;

import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//TODO: add negative test cases
//TODO: Add Mocking because all test are dependent on correct constructor initialization
public class ContactRepositoryTest {

    private static final String CONTACTS_FILE_PATH = "src/test/resources/contacts.csv";
    private static final String CONTACTS_TEST_FILE_PATH = "src/test/resources/contacts_testdata.csv";

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

    private void resetTestData() throws IOException {
        Files.copy( Paths.get(CONTACTS_TEST_FILE_PATH), Paths.get(CONTACTS_FILE_PATH), StandardCopyOption.REPLACE_EXISTING);
    }

    private void initializeRepository() throws Exception{
        contactRepository = new ContactRepository(CONTACTS_FILE_PATH);
        contactRepository.loadContacts();
    }

    @Test
    void constructor_ValidAttributes_ListCreated(){
        //TODO: Set Arrange and Act by including mocking and moving setup-method here
        //Assert
        assertEquals(INITIAL_LIST_SIZE, contactRepository.getContactList().size());
        assertContactListAttributes(FIRST_CONTACT, contactRepository.getContactList().get(0));
        assertContactListAttributes(SECOND_CONTACT, contactRepository.getContactList().get(1));
    }

    @Test
    void addToSelectedContacts_ValidAttributes_ItemCreated(){
        //Arrange & Act
        contactRepository.addToSelectedContacts(FIRST_CONTACT.getEmail());

        //Assert selected contacts size increased by one
        assertEquals(INITIAL_SELECTED_SIZE + 1, contactRepository.getSelectedContacts().size());
    }

    //TODO: Add Mocking, dependent on adding a contact
    @Test
    void removeFromSelectedContacts_ValidAttributes_ItemRemoved(){
        //Arrange
        contactRepository.addToSelectedContacts(FIRST_CONTACT.getEmail());

        //Act
        contactRepository.removeFromSelectedContacts(FIRST_CONTACT.getEmail());

        //Assert selected contacts size decreased by one
        assertEquals(INITIAL_SELECTED_SIZE, contactRepository.getSelectedContacts().size());
    }

    @Test
    void setProfile_ValidAttributes_ProfileCreated(){
        //Arrange & Act
        contactRepository.setProfile(FIRST_CONTACT.getEmail());

        //Assert contact list size remains unchanged
        assertEquals(INITIAL_LIST_SIZE, contactRepository.getContactList().size());
        //Assert selected contacts size increased by one
        assertEquals(INITIAL_SELECTED_SIZE + 1, contactRepository.getSelectedContacts().size());
        //Assert profile contact reference
        assertContactListAttributes(FIRST_CONTACT, contactRepository.getProfile());
        //Assert selected contact list reference
        assertContactListAttributes(FIRST_CONTACT, contactRepository.getSelectedContacts().get(0));
    }

    @Test
    void setNewProfile_ValidAttributes_ProfileCreated() throws Exception{
        //Arrange & Act
        contactRepository.setNewProfile(NEW_CONTACT);

        //Assert contact list size increased by one
        assertEquals(INITIAL_LIST_SIZE + 1, contactRepository.getContactList().size());
        //Assert selected contacts size increased by one
        assertEquals(INITIAL_SELECTED_SIZE + 1, contactRepository.getSelectedContacts().size());
        //Assert profile contact reference
        assertContactListAttributes(NEW_CONTACT, contactRepository.getProfile());
        //Assert selected contact list reference
        assertContactListAttributes(NEW_CONTACT, contactRepository.getSelectedContacts().get(0));
        //Assert contact list reference
        assertContactListAttributes(NEW_CONTACT, contactRepository.getContactList().get(2));
    }

    //TODO: Add mocking, dependent on first setProfile method
    @Test
    void replaceProfile_ValidAttributes_ProfileReplaced(){
        //Arrange & Act
        contactRepository.setProfile(FIRST_CONTACT.getEmail());
        contactRepository.setProfile(SECOND_CONTACT.getEmail());

        //Assert contact list size remains unchanged
        assertEquals(INITIAL_LIST_SIZE, contactRepository.getContactList().size());
        //Assert Selected contacts size increased by one and not two
        assertEquals(INITIAL_SELECTED_SIZE + 1, contactRepository.getSelectedContacts().size());
        //Assert profile contact reference
        assertContactListAttributes(SECOND_CONTACT, contactRepository.getProfile());
        //Assert selected contact list reference
        assertContactListAttributes(SECOND_CONTACT, contactRepository.getSelectedContacts().get(0));
    }

    @Test
    void addContact_ValidAttributes_ContactCreated() throws Exception{
        //Arrange & Act
        contactRepository.addContact(NEW_CONTACT);

        //Assert contact list size increased by one
        assertEquals(INITIAL_LIST_SIZE + 1, contactRepository.getContactList().size());
        //Assert selected contact list size remains unchanged
        assertEquals(INITIAL_SELECTED_SIZE, contactRepository.getSelectedContacts().size());
        //Assert contact list reference
        assertContactListAttributes(NEW_CONTACT, contactRepository.getContactList().get(2));
    }

    //TODO: Add mocking, dependent on addToSelectedContacts method
    @Test
    void removeContact_ValidAttributes_ContactRemoved() throws Exception{
        //Arrange
        contactRepository.addToSelectedContacts(FIRST_CONTACT.getEmail());

        //Act
        contactRepository.removeContact(FIRST_CONTACT.getEmail());

        //Assert contact list decreased by one
        assertEquals(INITIAL_LIST_SIZE - 1, contactRepository.getContactList().size());
        //Assert selected contact list decreased by one
        assertEquals(INITIAL_SELECTED_SIZE, contactRepository.getSelectedContacts().size());
        //Assert contact list reference
        assertContactListAttributes(SECOND_CONTACT, contactRepository.getContactList().get(0));
    }

    @Test
    void removeContact_IsProfile_ThrowsException(){
        //Arrange
        contactRepository.setProfile(FIRST_CONTACT.getEmail());

        //Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> contactRepository.removeContact(FIRST_CONTACT.getEmail()));
        assertEquals(exception.getMessage(), "Contact is selected profile");
    }

    //TODO: Add Mocking, dependent on adding contact
    @Test
    void updateContact_ValidAttribute_ContactUpdated() throws Exception{
        //Arrange
        contactRepository.addToSelectedContacts(FIRST_CONTACT.getEmail());
        contactRepository.setProfile(FIRST_CONTACT.getEmail());

        //Act
        contactRepository.updateContact(FIRST_CONTACT.getEmail(), NEW_CONTACT);

        //Assert contact List size remains unchanged
        assertEquals(INITIAL_LIST_SIZE, contactRepository.getContactList().size());
        //Assert selected contacts size should remains unchanged
        assertEquals(INITIAL_SELECTED_SIZE + 1, contactRepository.getSelectedContacts().size());
        //Assert contact list reference
        assertContactListAttributes(NEW_CONTACT, contactRepository.getContactList().get(0));
        //Assert selected contact list reference
        assertContactListAttributes(NEW_CONTACT, contactRepository.getSelectedContacts().get(0));
        //Assert profile reference
        assertContactListAttributes(NEW_CONTACT, contactRepository.getProfile());
    }

    private void assertContactListAttributes(Contact expected, Contact actual){
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getEmail(), actual.getEmail());
    }
}