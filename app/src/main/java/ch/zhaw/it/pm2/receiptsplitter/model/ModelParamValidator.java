package ch.zhaw.it.pm2.receiptsplitter.model;

//TODO: Custom Exception

/**
 * This class provides static methods to validate the parameters of the model classes.
 */
class ModelParamValidator {

    /**
     * Throws an IllegalArgumentException if the index is out of bounds.
     * @param index The index to check.
     * @param size The size of the list.
     * @param errorMessage The error message to throw.
     */
    static void throwIfIndexOutOfBounds(int index, int size, String errorMessage){
        if (index < 0 || index >= size)
            throw new IllegalArgumentException(errorMessage);
    }

    /**
     * Throws an IllegalArgumentException if the element is null.
     * @param object The object to check.
     * @param errorMessage The error message to throw.
     */
    static void throwIfElementIsNull(Object object, String errorMessage){
        if(object == null)
            throw new IllegalArgumentException(errorMessage);
    }

    /**
     * Throws an IllegalArgumentException if the amount is zero or lower.
     * @param amount The amount to check.
     * @param errorMessage The error message to throw.
     */
    static void throwIfZeroOrLower(float amount, String errorMessage){
        if (amount <= 0)
            throw new IllegalArgumentException(errorMessage);
    }

    /**
     * Throws an IllegalArgumentException if the string is empty.
     * @param string The string to check.
     * @param errorMessage The error message to throw.
     */
    static void throwIfStringIsEmpty(String string, String errorMessage){
        throwIfElementIsNull(string, errorMessage);
        if (string.trim().isEmpty())
            throw new IllegalArgumentException(errorMessage);
    }

    /**
     * Throws an IllegalArgumentException if the email is invalid.
     * @param email The email to check.
     * @param errorMessage The error message to throw.
     */
    static void throwIfEmailIsInvalid(String email, String errorMessage){
        throwIfElementIsNull(email, errorMessage);
        if (!email.trim().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"))
            throw new IllegalArgumentException(errorMessage);
    }
}
