package ch.zhaw.it.pm2.receiptsplitter.model;

//TODO: JavaDoc
//TODO: Custom Exception
public class ModelParamValidator {

    static void throwIfIndexOutOfBounds(int index, int size, String errorMessage){
        if (index < 0 || index >= size)
            throw new IllegalArgumentException(errorMessage);
    }

    static void throwIfElementIsNull(Object object, String errorMessage){
        if(object == null)
            throw new IllegalArgumentException(errorMessage);
    }

    static void throwIfZeroOrLower(float amount, String errorMessage){
        if (amount <= 0)
            throw new IllegalArgumentException(errorMessage);
    }

    static void throwIfStringIsEmpty(String string, String errorMessage){
        throwIfElementIsNull(string, errorMessage);
        if (string.trim().isEmpty())
            throw new IllegalArgumentException(errorMessage);
    }

    static void throwIfEmailIsInvalid(String email, String errorMessage){
        throwIfElementIsNull(email, errorMessage);
        if (!email.trim().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"))
            throw new IllegalArgumentException(errorMessage);
    }
}
