package ch.zhaw.it.pm2.receiptsplitter.repository;

import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class ContactRepositoryTest {

    private static final Path CONTACTS_FILE_PATH = Paths.get("src/test/resources/contacts.csv");
    private static final Path CONTACTS_TEST_FILE_PATH = Paths.get("src/test/resources/contacts_testdata.csv");

    @Mock
    private Contact firstContact;

    @Mock
    private Contact secondContact;

    @Mock
    private Contact newContact;

    private static final int INITIAL_LIST_SIZE = 2;
    private static final int INITIAL_SELECTED_SIZE = 0;

    private ContactRepository contactRepository;

    @BeforeEach
    void setUp() throws Exception{
        generateMockContact(firstContact, "John", "Doe", "John.Doe@example.com");
        generateMockContact(secondContact, "Max", "Mustermann", "Max.Mustermann@example.com");
        generateMockContact(newContact, "Third", "Contact", "Third.Contact@example.com");

        resetTestData();
        initializeRepository();
    }

    //lenient is used here to prevent code duplication and further improve maintainability
    private void generateMockContact(Contact mockContact, String firstName, String lastName, String email){
        lenient().when(mockContact.getFirstName()).thenReturn(firstName);
        lenient().when(mockContact.getLastName()).thenReturn(lastName);
        lenient().when(mockContact.getEmail()).thenReturn(email);
    }

    private void resetTestData() throws IOException {
        if(!Files.exists(CONTACTS_FILE_PATH))
            createTestData();

        if(!Files.exists(CONTACTS_TEST_FILE_PATH))
            Files.createFile(CONTACTS_TEST_FILE_PATH);

        Files.copy( CONTACTS_FILE_PATH, CONTACTS_TEST_FILE_PATH, StandardCopyOption.REPLACE_EXISTING);
    }

    private void createTestData() throws IOException {
        Files.createFile(CONTACTS_FILE_PATH);
        Files.write(CONTACTS_FILE_PATH, ("John;Doe;John.Doe@example.com\n" +
                        "Max;Mustermann;Max.Mustermann@example.com").getBytes());
    }

    private void initializeRepository() throws Exception{
        contactRepository = new ContactRepository(CONTACTS_TEST_FILE_PATH.toString());
        contactRepository.loadContacts();
    }

    @Test
    void constructor_ValidAttributes_ListCreated(){
        //Assert
        assertEquals(INITIAL_LIST_SIZE, contactRepository.getContacts().size());
        assertContactListAttributes(firstContact, contactRepository.getContacts().get(0));
        assertContactListAttributes(secondContact, contactRepository.getContacts().get(1));
    }

    @Test
    void addToSelectedContacts_ValidAttributes_ItemCreated(){
        //Arrange & Act
        contactRepository.addToSelectedContacts(firstContact.getEmail());

        //Assert selected contacts size increased by one
        assertEquals(INITIAL_SELECTED_SIZE + 1, contactRepository.getSelectedContacts().size());
    }

    @Test
    void removeFromSelectedContacts_ValidAttributes_ItemRemoved(){
        //Arrange
        contactRepository.addToSelectedContacts(firstContact.getEmail());

        //Act
        contactRepository.removeFromSelectedContacts(firstContact.getEmail());

        //Assert selected contacts size decreased by one
        assertEquals(INITIAL_SELECTED_SIZE, contactRepository.getSelectedContacts().size());
    }

    @Test
    void setProfile_ValidAttributes_ProfileCreated(){
        //Arrange & Act
        contactRepository.setProfile(firstContact.getEmail());

        //Assert contact list size remains unchanged
        assertEquals(INITIAL_LIST_SIZE, contactRepository.getContacts().size());
        //Assert selected contacts size increased by one
        assertEquals(INITIAL_SELECTED_SIZE + 1, contactRepository.getSelectedContacts().size());
        //Assert profile contact reference
        assertContactListAttributes(firstContact, contactRepository.getProfile());
        //Assert selected contact list reference
        assertContactListAttributes(firstContact, contactRepository.getSelectedContacts().getLast());
    }

    @Test
    void setNewProfile_ValidAttributes_ProfileCreated() throws Exception{
        //Arrange & Act
        contactRepository.setNewProfile(newContact);

        //Assert contact list size increased by one
        assertEquals(INITIAL_LIST_SIZE + 1, contactRepository.getContacts().size());
        //Assert selected contacts size increased by one
        assertEquals(INITIAL_SELECTED_SIZE + 1, contactRepository.getSelectedContacts().size());
        //Assert profile contact reference
        assertContactListAttributes(newContact, contactRepository.getProfile());
        //Assert selected contact list reference
        assertContactListAttributes(newContact, contactRepository.getSelectedContacts().getLast());
        //Assert contact list reference
        assertContactListAttributes(newContact, contactRepository.getContacts().getLast());
    }

    @Test
    void replaceProfile_ValidAttributes_ProfileReplaced(){
        //Arrange & Act
        contactRepository.setProfile(firstContact.getEmail());
        contactRepository.setProfile(secondContact.getEmail());

        //Assert contact list size remains unchanged
        assertEquals(INITIAL_LIST_SIZE, contactRepository.getContacts().size());
        //Assert Selected contacts size increased by one and not two
        assertEquals(INITIAL_SELECTED_SIZE + 1, contactRepository.getSelectedContacts().size());
        //Assert profile contact reference
        assertContactListAttributes(secondContact, contactRepository.getProfile());
        //Assert selected contact list reference
        assertContactListAttributes(secondContact, contactRepository.getSelectedContacts().getLast());
    }

    @Test
    void addContact_ValidAttributes_ContactCreated() throws Exception{
        //Arrange & Act
        contactRepository.addContact(newContact);

        //Assert contact list size increased by one
        assertEquals(INITIAL_LIST_SIZE + 1, contactRepository.getContacts().size());
        //Assert selected contact list size remains unchanged
        assertEquals(INITIAL_SELECTED_SIZE, contactRepository.getSelectedContacts().size());
        //Assert contact list reference
        assertContactListAttributes(newContact, contactRepository.getContacts().getLast());
    }

    @Test
    void removeContact_ValidAttributes_ContactRemoved() throws Exception{
        //Arrange
        contactRepository.addToSelectedContacts(firstContact.getEmail());

        //Act
        contactRepository.removeContact(firstContact.getEmail());

        //Assert contact list decreased by one
        assertEquals(INITIAL_LIST_SIZE - 1, contactRepository.getContacts().size());
        //Assert selected contact list decreased by one
        assertEquals(INITIAL_SELECTED_SIZE, contactRepository.getSelectedContacts().size());
        //Assert contact list reference
        assertContactListAttributes(secondContact, contactRepository.getContacts().getLast());
    }

    @Test
    void removeContact_IsProfile_ThrowsException(){
        //Arrange
        contactRepository.setProfile(firstContact.getEmail());

        //Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> contactRepository.removeContact(firstContact.getEmail()));
        assertEquals(exception.getMessage(), "Contact is selected profile");
    }

    @Test
    void updateContact_ValidAttribute_ContactUpdated() throws Exception{
        //Arrange
        contactRepository.addToSelectedContacts(firstContact.getEmail());
        contactRepository.setProfile(firstContact.getEmail());

        //Act
        contactRepository.updateContact(firstContact.getEmail(), newContact);

        //Assert contact List size remains unchanged
        assertEquals(INITIAL_LIST_SIZE, contactRepository.getContacts().size());
        //Assert selected contacts size should remain unchanged
        assertEquals(INITIAL_SELECTED_SIZE + 1, contactRepository.getSelectedContacts().size());
        //Assert contact list reference
        assertContactListAttributes(newContact, contactRepository.getContacts().getFirst());
        //Assert selected contact list reference
        assertContactListAttributes(newContact, contactRepository.getSelectedContacts().getFirst());
        //Assert profile reference
        assertContactListAttributes(newContact, contactRepository.getProfile());
    }

    private void assertContactListAttributes(Contact expected, Contact actual){
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getEmail(), actual.getEmail());
    }

    @Test
    void addToSelectedContacts_InvalidContact_ContactNotAdded(){
        //Arrange
        int listSize = contactRepository.getSelectedContacts().size();
        //Act
        contactRepository.addToSelectedContacts(newContact.getEmail());
        //Assert
        assertEquals(listSize, contactRepository.getSelectedContacts().size());
    }

    @Test
    void removeFromSelectedContacts_InvalidContact_ContactNotRemoved(){
        //Arrange
        int listSize = contactRepository.getSelectedContacts().size();
        //Arrange & Act & Assert
        contactRepository.removeFromSelectedContacts(firstContact.getEmail());
        //Assert
        assertEquals(listSize, contactRepository.getSelectedContacts().size());
    }

    @Test
    void setProfile_InvalidContact_ProfileNotSet(){
        //Arrange & Act & Assert
        assertFalse(contactRepository.setProfile(newContact.getEmail()));
    }

    @Test
    void addContact_DuplicateContact_ContactNotCreated(){
        //Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> contactRepository.addContact(firstContact));
    }

    @Test
    void removeContact_NonExistentContact_ContactNotRemoved(){
        //Arrange
        int listSize = contactRepository.getContacts().size();
        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> contactRepository.removeContact(newContact.getEmail()));
        assertEquals(listSize, contactRepository.getContacts().size());
    }

    @Test
    void updateContact_NonExistentContact_ContactNotUpdated(){
        //Arrange
        int listSize = contactRepository.getContacts().size();
        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> contactRepository.updateContact(newContact.getEmail(), firstContact));
        assertEquals(listSize, contactRepository.getContacts().size());
    }
}