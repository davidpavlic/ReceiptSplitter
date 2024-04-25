package ch.zhaw.it.pm2.receiptsplitter.model;

//TODO: JavaDoc
public class Contact implements CanValidateModelParam {

    private String firstName;
    private String lastName;
    private String email;

    private static final String FIRST_NAME_ERROR_MESSAGE   = "First name must not be empty.";
    private static final String LAST_NAME_ERROR_MESSAGE    = "Last name must not be null.";
    private static final String EMAIL_ERROR_MESSAGE        = "Email must be a valid email address.";

    public Contact(String firstName, String lastName, String email) throws IllegalArgumentException{
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) throws IllegalArgumentException{
        throwIfStringIsEmpty(firstName, FIRST_NAME_ERROR_MESSAGE);
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) throws IllegalArgumentException{
        // lastName can be empty
        throwIfElementIsNull(lastName, LAST_NAME_ERROR_MESSAGE);
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws IllegalArgumentException{
        throwIfEmailIsInvalid(email, EMAIL_ERROR_MESSAGE);
        this.email = email;
    }
}