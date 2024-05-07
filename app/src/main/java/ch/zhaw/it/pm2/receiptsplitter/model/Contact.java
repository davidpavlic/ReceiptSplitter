package ch.zhaw.it.pm2.receiptsplitter.model;

/**
 * This class represents a contact with a first name, last name, and email.
 * It contains methods to get and set these properties.
 * The first name cannot be empty, the last name can be empty but not null, and the email must be valid.
 *
 * @version 1.0
 * @author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 */
public class Contact {

    private String firstName;
    private String lastName;
    private String email;

    /**
     * Constructs a new Contact instance.
     *
     * @param firstName the first name of the contact
     * @param lastName  the last name of the contact
     * @param email     the email of the contact
     * @throws IllegalArgumentException if the first name is empty, or if the last name is null, or if the email is invalid
     */
    public Contact(String firstName, String lastName, String email) throws IllegalArgumentException {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
    }

    /**
     * Gets the first name of the contact.
     * @return the first name of the contact
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the contact.
     *
     * @param firstName the first name of the contact
     * @throws IllegalArgumentException if the first name is empty
     */
    public void setFirstName(String firstName) throws IllegalArgumentException {
        ModelParamValidator.throwIfStringIsEmpty(firstName, ContactErrorMessageType.FIRST_NAME_EMPTY.toString());
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the contact.
     * @return the last name of the contact
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the contact.
     *
     * @param lastName the last name of the contact
     * @throws IllegalArgumentException if the last name is null
     */
    public void setLastName(String lastName) throws IllegalArgumentException {
        // lastName can be empty
        ModelParamValidator.throwIfElementIsNull(lastName, ContactErrorMessageType.LAST_NAME_NULL.toString());
        this.lastName = lastName;
    }

    /**
     * Gets the email of the contact.
     * @return the email of the contact
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the contact.
     *
     * @param email the email of the contact
     * @throws IllegalArgumentException if the email is invalid
     */
    public void setEmail(String email) throws IllegalArgumentException {
        ModelParamValidator.throwIfEmailIsInvalid(email, ContactErrorMessageType.EMAIL_INVALID.toString());
        this.email = email;
    }

    /**
     * Returns the display name of the contact.
     * @return the display name of the contact
     */
    public String getDisplayName() {
        return firstName + " " + lastName;
    }

    /**
     * This enum represents the types of error messages that can be thrown by the Contact class.
     */
    protected enum ContactErrorMessageType {
        FIRST_NAME_EMPTY("First name must not be empty."),
        LAST_NAME_NULL("Last name must not be null."),
        EMAIL_INVALID("Email must be a valid email address.");

        private final String message;

        ContactErrorMessageType(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }
}