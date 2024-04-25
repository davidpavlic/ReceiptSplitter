package ch.zhaw.it.pm2.receiptsplitter.model;

//TODO: Javadoc
public interface CanValidateModelParam {

    default void throwIfIndexIsNotPresent(int index, int size, String message){
        if (index < 0 || index >= size)
            throw new IllegalArgumentException(message);
    }

    default void throwIfElementIsNull(Object object, String message) throws IllegalArgumentException{
        if(object == null)
            throw new IllegalArgumentException(message);
    }

    default void throwIfZeroOrLower(float amount, String message){
        if (amount <= 0)
            throw new IllegalArgumentException(message);
    }

    default void throwIfStringIsEmpty(String string, String message){
        throwIfElementIsNull(string, message);
        if (string.trim().isEmpty())
            throw new IllegalArgumentException(message);
    }

    default void throwIfEmailIsInvalid(String email, String message) {
        throwIfElementIsNull(email, message);
        if (!email.trim().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"))
            throw new IllegalArgumentException(message);
    }
}
