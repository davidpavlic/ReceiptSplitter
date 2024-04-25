package ch.zhaw.it.pm2.receiptsplitter.model;

/**
 * Validates the parameters of the model of the receiptsplitter and throws an IllegalArgumentException if invalid.
 *
 * @author David Pavlic, Suhejl Asani, Ryan Simmonds, Kaspar Streiff
 * @version 1.0
 */
public interface CanValidateModelParam {

    /**
     * Validates if the index is smaller than the size and bigger than zero.
     */
    default void throwIfIndexIsNotPresent(int index, int size, String message){
        if (index < 0 || index >= size)
            throw new IllegalArgumentException(message);
    }

    /**
     * Validates if the object is not null.
     */
    default void throwIfElementIsNull(Object object, String message){
        if(object == null)
            throw new IllegalArgumentException(message);
    }

    /**
     * Validates if the amount is bigger then zero
     */
    default void throwIfZeroOrLower(float amount, String message){
        if (amount <= 0)
            throw new IllegalArgumentException(message);
    }

    /**
     * Validates if the string is not null and not empty
     */
    default void throwIfStringIsEmpty(String string, String message){
        throwIfElementIsNull(string, message);
        if (string.trim().isEmpty())
            throw new IllegalArgumentException(message);
    }

    /**
     * Validates if the email is not null and follows an email standard
     */
    default void throwIfEmailIsInvalid(String email, String message){
        throwIfElementIsNull(email, message);
        if (!email.trim().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"))
            throw new IllegalArgumentException(message);
    }
}
