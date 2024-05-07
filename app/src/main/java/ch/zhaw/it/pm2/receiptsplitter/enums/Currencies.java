package ch.zhaw.it.pm2.receiptsplitter.enums;

/**
 * This enum represents the currencies that can be used in the application.
 * It contains the currency code.
 *
 * @version 1.0
 * @author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 */
public enum Currencies {
    CHF("CHF");
    // More currencies can be added here

    private final String currency;

    /**
     * Constructs a new Currencies instance with the currency code.
     * @param currency the currency code
     */
    Currencies(String currency) {
        this.currency = currency;
    }

    /**
     * Gets the currency code.
     * @return the currency code
     */
    public String getCurrency() {
        return currency;
    }
}
