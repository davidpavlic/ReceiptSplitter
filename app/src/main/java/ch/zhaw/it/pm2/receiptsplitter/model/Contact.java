package ch.zhaw.it.pm2.receiptsplitter.model;

//TODO: JavaDoc
public class Contact {

    private String firstName;
    private String lastName;
    private String email;

    public Contact(String firstName, String lastName, String email) throws IllegalArgumentException{
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) throws IllegalArgumentException{
        ModelParamValidator.throwIfStringIsEmpty(firstName, ContactErrorMessageType.FIRST_NAME_EMPTY.toString());
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) throws IllegalArgumentException{
        // lastName can be empty
        ModelParamValidator.throwIfElementIsNull(lastName, ContactErrorMessageType.LAST_NAME_NULL.toString());
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws IllegalArgumentException{
        ModelParamValidator.throwIfEmailIsInvalid(email, ContactErrorMessageType.EMAIL_INVALID.toString());
        this.email = email;
    }

    public enum ContactErrorMessageType {
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